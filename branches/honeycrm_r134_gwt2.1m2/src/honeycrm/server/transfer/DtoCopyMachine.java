package honeycrm.server.transfer;

import honeycrm.client.dto.Dto;
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.CommonServiceReader;
import honeycrm.server.DomainClassRegistry;
import honeycrm.server.PMF;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.UniqueService;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

public class DtoCopyMachine {
	private static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private static final DomainClassRegistry registry = DomainClassRegistry.instance;
	private static PersistenceManager m = null;

	public AbstractEntity copy(Dto dto) {
		return copy(dto, null);
	}

	/**
	 * Copy data from Dto into domain class. This is usually the case whenever the client sends data to the server (create, update)
	 */
	public AbstractEntity copy(final Dto dto, final AbstractEntity existingEntity) {
		final Class<? extends AbstractEntity> entityClass = registry.getDomain(dto.getModule());

		try {
			final AbstractEntity entity = entityClass.newInstance();
			final Field[] allFields = FieldSieve.instance.filterFields(reflectionHelper.getAllFields(entityClass));
			final boolean entityAlreadyExists = null != existingEntity;

			if (entityAlreadyExists) {
				/**
				 * this entity already exists so we can safely copy the id field now.
				 */
				entity.id = existingEntity.id;
			}

			for (final Field field : allFields) {
				copySingleField(dto, existingEntity, entityClass, entity, entityAlreadyExists, field);
			}

			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copies one field from the dto / existing entity into the new / updated entity.
	 */
	private void copySingleField(final Dto dto, final AbstractEntity existingEntity, final Class<? extends AbstractEntity> entityClass, final AbstractEntity entity, final boolean entityAlreadyExists, final Field field) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		final String fieldName = field.getName();

		if ("id".equals(fieldName)) {
			/**
			 * skip the id field because we already copied the id before.
			 */
			return;
		}
		
		final Object value = getValue(dto, existingEntity, entityAlreadyExists, field, fieldName);
		
		if (DtoWizard.instance.getRelateFields().containsKey(field.hashCode())) {
			// treat one to many relate field special
			deleteOldChildItems(entityClass, entity, field);
			handleDtoLists(entityClass, entity, field, fieldName, value);
		} else {
			if (null == value) {
				/**
				 * skip this field since it is null anyway. the user cannot set fields null so we can safely skip updating this field.
				 */
			} else if (value instanceof List<?>) {
				// TODO remove clause
				throw new RuntimeException("Should not reach this point anymore");
			} else {
				/**
				 * finally we found a good old normal field. copy the value from the dto / existing entity into the new / updated entity.
				 */
				try {
					field.set(entity, value);
				} catch (IllegalArgumentException e) {
					System.err.println("asdas");
				}
			}
		}
	}

	private Object getValue(final Dto dto, final AbstractEntity existingEntity, final boolean entityAlreadyExists, final Field field, final String fieldName) throws IllegalAccessException {
		final Object value;
		if (entityAlreadyExists) {
			if (null == dto.get(fieldName)) {
				value = field.get(existingEntity);
			} else {
				value = dto.get(fieldName);
			}
		} else {
			value = dto.get(fieldName);
		}
		return value;
	}

	private void handleDtoLists(final Class<? extends AbstractEntity> entityClass, final AbstractEntity entity, final Field field, final String fieldName, final Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (null == value || ((List<?>) value).isEmpty() || !(((List<?>) value).get(0) instanceof Dto)) {
			return; // do not do anything because of the content stored in value.
		}

		if (null == m) {
			m = PMF.get().getPersistenceManager();
		}

		final List<Key> keys = new ArrayList<Key>();

		for (final Dto child : (List<Dto>) value) {
			/**
			 * save the child item externally add its Key to the key list which is stored within the parent.
			 */
			final AbstractEntity childDomainObject = copy(child);
			m.makePersistent(childDomainObject);
			keys.add(childDomainObject.id);
		}

		// do not set field anymore because child will be stored externally
		// field.set(entity, serverList);
		// store child keys in entity
		try {
			field.set(entity, keys);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copy data from a domain class instance into a Dto. This is usually the case whenever the server responds to clients (read).
	 */
	// application hotspot 2nd place
	public Dto copy(AbstractEntity entity) {
		final Dto dto = new Dto();

		if (null == entity) {
			System.out.println("npe");
		}
		final Class<?> entityClass = entity.getClass();
		final Field[] allFields = FieldSieve.instance.filterFields(reflectionHelper.getAllFields(entityClass));

		try {
			for (final Field field : allFields) {
				final String fieldName = field.getName();
				final Object value = field.get(entity);

				if (null == value) {
					/**
					 * skip null values
					 */
				} else if (DtoWizard.instance.getRelateFields().containsKey(field.hashCode())) {
					handleDomainClassLists(dto, field, (List<?>) value);
				} else if ("id".equals(fieldName)) {
					try {
						dto.set(fieldName, (int) ((Key) value).getId());
					} catch (NullPointerException e) {
						System.out.println("npe!");
					}
				} else {
					dto.set(fieldName, (Serializable) value);
				}
			}

			dto.setModule(entityClass.getSimpleName().toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	private void handleDomainClassLists(final Dto dto, final Field field, final List<?> value) {
		if (!value.isEmpty() && value.get(0) instanceof Key) {
			if (null == m) {
				m = PMF.get().getPersistenceManager();
			}

			// retrieve the children whose keys have been stored and insert them into the dto object.
			final LinkedList<Dto> children = new LinkedList<Dto>();
			for (final Key key : (List<Key>) value) {
				final AbstractEntity childDomainObject = m.getObjectById(DtoWizard.instance.getRelateFields().get(field.hashCode()), key.getId());
				final Dto childDto = copy(childDomainObject);
				
				CommonServiceReader.resolveRelatedEntities(childDomainObject, childDto);
				
				children.add(childDto);
			}

			// TODO set name in a generic manner
			dto.set(field.getName(), children);

			return; // skip key lists because we cannot serialize them
		}

		final List<Dto> serverList = new LinkedList<Dto>();

		for (final AbstractEntity child : (List<AbstractEntity>) value) {
			/**
			 * resolving related entities of child entities. e.g. services are child items of offerings and contracts. this is necessary to display the name of the related entity (and other fields that might be interesting).
			 */
			final Dto childDto = copy(child);
			CommonServiceReader.resolveRelatedEntities(child, childDto);

			serverList.add(childDto);
		}

		dto.set(field.getName(), (Serializable) serverList);
	}

	/**
	 * TODO This removes the child elements of an entity during an update to avoid duplicating them. This should not be under the responsibility of this class and has to be refactored in the future!
	 */
	private void deleteOldChildItems(final Class<? extends AbstractEntity> entityClass, final AbstractEntity existingEntity, final Field field) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null != existingEntity) {
			// This is an update, before doing anything else we remove the existing items linked to this entity
			// This avoids duplicating them on an update.
			// Note this means that on each update the linked items of an entity are deleted and recreated. This should be avoided in future versions.
			final Collection<?> existingItemsList = (Collection<?>) field.get(existingEntity);

			if (null == existingItemsList || existingItemsList.isEmpty()) {
				return; // we do not have to do anything
			}

			if (null == m) {
				m = PMF.get().getPersistenceManager();
			}

			if (DtoWizard.instance.getRelateFields().containsKey(field.hashCode())) {
				// TODO does this work? delete specifying a key collection
				m.deletePersistentAll(DtoWizard.instance.getRelateFields().get(field.hashCode()), existingItemsList);
			}

			/*
			 * for (final Key childId : (List<Key>) existingItemsList) { if (null == childId) { System.out.println("epic fail: prevented npe"); } else { m.deletePersistent(m.getO)); // TODO why doesn't that work too? this throws an "this entity is currently managed by another manager" exception. // m.deletePersistent(child); // m.deletePersistent(m.getObjectById(child.getClass(), KeyFactory.createKey(existingEntity.id, child.getClass().getSimpleName(), child.id.getId()))); } }
			 */
		}
	}
}
