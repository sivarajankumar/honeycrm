package honeycrm.server.transfer;

import honeycrm.client.dto.Dto;
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.CommonServiceReader;
import honeycrm.server.DomainClassRegistry;
import honeycrm.server.PMF;
import honeycrm.server.domain.AbstractEntity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
		} else {
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

			if (null == value) {
				/**
				 * skip this field since it is null anyway. the user cannot set fields null so we can safely skip updating this field.
				 */
			} else if (value instanceof List<?>) {
				deleteOldChildItems(entityClass, entity, field);
				handleDtoLists(entityClass, entity, field, value);
			} else {
				/**
				 * finally we found a good old normal field. copy the value from the dto / existing entity into the new / updated entity.
				 */
				field.set(entity, value);
			}
		}
	}

	private void handleDtoLists(final Class<? extends AbstractEntity> entityClass, final AbstractEntity entity, final Field field, final Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (null == value || ((List<?>) value).isEmpty() || !(((List<?>) value).get(0) instanceof Dto)) {
			return; // do not do anything because of the content stored in value.
		}

		final List<AbstractEntity> serverList = new LinkedList<AbstractEntity>();

		for (final Dto child : (List<Dto>) value) {
			serverList.add(copy(child));
		}

		field.set(entity, serverList);
	}

	/**
	 * Copy data from a domain class instance into a Dto. This is usually the case whenever the server responds to clients (read).
	 */
	// application hotspot 2nd place
	public Dto copy(AbstractEntity entity) {
		final Dto dto = new Dto();

		final Class<?> entityClass = entity.getClass();
		final Field[] allFields = FieldSieve.instance.filterFields(reflectionHelper.getAllFields(entityClass));

		try {
			for (final Field field: allFields) {
				final String fieldName = field.getName();
				final Object value = field.get(entity);

				if (null == value) {
					/**
					 * skip null values
					 */
				} else if (value instanceof List<?>) {
					handleDomainClassLists(dto, field, value);
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

	private void handleDomainClassLists(final Dto dto, final Field field, final Object value) {
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
			final List<?> existingItemsList = (List<?>) field.get(existingEntity);

			if (null == existingItemsList) {
				return; // we do not have to do anything
			}

			// if (existingItemsList.get(0) instanceof AbstractEntity) {
			if (null == m) {
				m = PMF.get().getPersistenceManager();
			}

			for (final AbstractEntity child : (List<AbstractEntity>) existingItemsList) {
				if (null == child.id) {
					System.out.println("epic fail: prevented npe");
				} else {
					// TODO why doesn't that work too? this throws an "this entity is currently managed by another manager" exception.
					// m.deletePersistent(child);
					m.deletePersistent(m.getObjectById(child.getClass(), KeyFactory.createKey(existingEntity.id, child.getClass().getSimpleName(), child.id.getId())));
				}
			}
			// }
		}
	}
}
