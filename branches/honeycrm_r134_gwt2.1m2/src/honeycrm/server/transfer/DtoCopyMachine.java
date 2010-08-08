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
import java.lang.reflect.Method;
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
	public AbstractEntity copy(Dto dto, AbstractEntity existingEntity) {
		final Class<? extends AbstractEntity> entityClass = registry.getDomain(dto.getModule());

		try {
			final AbstractEntity entity = entityClass.newInstance();
			final Field[] allFields = FieldSieve.instance.filterFields(reflectionHelper.getAllFields(entityClass));

			for (int i = 0; i < allFields.length; i++) {
				final Field field = allFields[i];
				final Object value = dto.get(field.getName());

				if (null == value) {
					continue;
				}

				if ("fields".equals(field.getName())) {
					continue;
				}

				if ("id".equals(field.getName())) {
					if (null == existingEntity) {
						continue;
					} else { //  if (null != value && (Long) value > 0)
						// insert id of the existing entity in the newly created one
						// this is an update
						final Method setter = entityClass.getMethod("setId", Key.class);
						setter.invoke(entity, existingEntity.getId());
						continue;
					}
				}

				if (value instanceof List<?>) {
					deleteOldChildItems(entityClass, existingEntity, field);
					handleDtoLists(entityClass, entity, field, value);
					continue;
				}

				final Method setter = entityClass.getMethod(reflectionHelper.getMethodNameCached(false, field), field.getType());
				setter.invoke(entity, value);
			}

			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void handleDtoLists(final Class<? extends AbstractEntity> entityClass, final AbstractEntity entity, final Field field, final Object value) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		if (((List<?>) value).get(0) instanceof Dto) {
			final List<AbstractEntity> serverList = new LinkedList<AbstractEntity>();

			for (final Dto child : (List<Dto>) value) {
				serverList.add(copy(child));
			}

			final Method setter = entityClass.getMethod(reflectionHelper.getMethodNameCached(false, field), List.class);
			setter.invoke(entity, serverList);
		}
	}

	/**
	 * Copy data from a domain class instance into a Dto. This is usually the case whenever the server responds to clients (read).
	 */
	public Dto copy(AbstractEntity entity) {
		final Dto dto = new Dto();

		final Class<?> entityClass = entity.getClass();
		final Field[] allFields = FieldSieve.instance.filterFields(reflectionHelper.getAllFields(entityClass));

		try {
			for (int i = 0; i < allFields.length; i++) {
				final Field field = allFields[i];

				final Method getter = entityClass.getMethod(reflectionHelper.getMethodNameCached(true, field));
				final Object value = getter.invoke(entity);

				if (null == value) {
					continue; // skip null values
				}
				
				if (value instanceof List<?>) {
					handleDomainClassLists(dto, field, value);
					continue;
				}

				if ("id".equals(field.getName())) {
					try {
						dto.set(field.getName(), (int) ((Key) value).getId());
					} catch (NullPointerException e) {
						System.out.println("npe!");
					}
				} else {
					dto.set(field.getName(), (Serializable) value);
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
			 * resolving related entities of child entities. e.g. services are child items of offerings and contracts.
			 * this is necessary to display the name of the related entity (and other fields that might be interesting).
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
	private void deleteOldChildItems(final Class<? extends AbstractEntity> entityClass, final AbstractEntity existingEntity, final Field field) throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null != existingEntity) {
			// This is an update, before doing anything else we remove the existing items linked to this entity
			// This avoids duplicating them on an update.
			// Note this means that on each update the linked items of an entity are deleted and recreated. This should be avoided in future versions.
			final List<?> existingItemsList = (List<?>) entityClass.getMethod(reflectionHelper.getMethodNameCached(true, field)).invoke(existingEntity);

			//if (existingItemsList.get(0) instanceof AbstractEntity) {
			if (null == m) {	
				m = PMF.get().getPersistenceManager();
			}
			
			for (final AbstractEntity child : (List<AbstractEntity>) existingItemsList) {
				if (null == child.getId()) {
					System.out.println("epic fail: prevented npe");
				} else {
					// TODO why doesn't that work too? this throws an "this entity is currently managed by another manager" exception.
					// m.deletePersistent(child);
					m.deletePersistent(m.getObjectById(child.getClass(), KeyFactory.createKey(existingEntity.getId(), child.getClass().getSimpleName(), child.getId().getId())));
				}
			}
			//}
		}
	}
}
