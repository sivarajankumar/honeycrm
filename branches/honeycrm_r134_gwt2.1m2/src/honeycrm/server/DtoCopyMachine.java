package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.AbstractEntity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DtoCopyMachine {
	private static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private static final Set<String> badVariableNames = new HashSet<String>();
	private static final Set<String> badVariablePrefixes = new HashSet<String>();
	private static final DomainClassRegistry registry = DomainClassRegistry.instance;
	private static PersistenceManager m = null;

	private static final Map<Integer, Field[]> cachedFilteredFields = new HashMap<Integer, Field[]>();
	
	static {
		badVariablePrefixes.add("$");
		badVariablePrefixes.add("jdo");

		badVariableNames.add("serialVersionUID");
	}

	public AbstractEntity copy(final Dto dto) {
		return copy(dto, null);
	}

	/**
	 * Copy data from Dto into domain class. This is usually the case whenever the client sends data to the server (create, update)
	 */
	public AbstractEntity copy(final Dto dto, final AbstractEntity existingEntity) {
		final Class<? extends AbstractEntity> entityClass = registry.getDomain(dto.getModule());

		try {
			final AbstractEntity entity = entityClass.newInstance();
			final Field[] allFields = getFilteredFieldsCached(reflectionHelper.getAllFields(entityClass));

			for (final Field field : allFields) {
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
					} else {
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

				final Method setter = entityClass.getMethod(reflectionHelper.getMethodName("set", field), field.getType());
				setter.invoke(entity, value);
			}

			return entity;
		} catch (final Exception e) {
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

			final Method setter = entityClass.getMethod(reflectionHelper.getMethodName("set", field), List.class);
			setter.invoke(entity, serverList);
		}
	}

	/**
	 * Copy data from a domain class instance into a Dto. This is usually the case whenever the server responds to clients (read).
	 */
	public Dto copy(final AbstractEntity entity) {
		final Dto dto = new Dto();

		final Class<?> entityClass = entity.getClass();
		final Field[] allFields = getFilteredFieldsCached(reflectionHelper.getAllFields(entityClass));

		try {
			for (final Field field : allFields) {
				final Method getter = entityClass.getMethod(reflectionHelper.getMethodName("get", field));
				final Object value = getter.invoke(entity);

				if (null == value) {
					continue;
				} else if (value instanceof List<?>) {
					handleDomainClassLists(dto, field, value);
					continue;
				} else if ("id".equals(field.getName())) {
					try {
						dto.set(field.getName(), (int) ((Key) value).getId());
					} catch (final NullPointerException e) {
						System.out.println("npe!");
					}
				} else {
					dto.set(field.getName(), (Serializable) value);
				}
			}

			dto.setModule(entityClass.getSimpleName().toLowerCase());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	private void handleDomainClassLists(final Dto dto, final Field field, final Object value) {
		final List<Dto> serverList = new LinkedList<Dto>();

		for (final AbstractEntity child : (List<AbstractEntity>) value) {
			serverList.add(copy(child));
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
			final List<?> existingItemsList = (List<?>) entityClass.getMethod(reflectionHelper.getMethodName("get", field)).invoke(existingEntity);

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

	/**
	 * Method throwing away all fields that are irrelevant and should not be copied by this copy machine (e.g. automatically added fields for serialization).
	 */
	private Field[] getFilteredFieldsCached(final Field[] allFields) {
		final int hashCode = allFields.hashCode();
		
		if (cachedFilteredFields.containsKey(hashCode)) {
			FilteredFieldsCacheStats.hits++;
			return cachedFilteredFields.get(hashCode);
		} else {
			FilteredFieldsCacheStats.misses++;
			final Field[] filtered = getFilteredFields(allFields);
			cachedFilteredFields.put(hashCode, filtered);
			return filtered;
		}
	}

	private Field[] getFilteredFields(final Field[] allFields) {
		final List<Field> filteredFields = new LinkedList<Field>();
		for (int i = 0; i < allFields.length; i++) {
			if (!shouldBeSkipped(allFields[i])) {
				filteredFields.add(allFields[i]);
			}
		}

		return filteredFields.toArray(new Field[0]);
	}

	/**
	 * Returns true if the field with the given name should be skipped during copying / DTO-DB comparison. False otherwise.
	 */
	private boolean shouldBeSkipped(final Field field) {
		if (Modifier.STATIC == (field.getModifiers() & Modifier.STATIC)) {
			return true; // skip static fields
		}

		if (badVariableNames.contains(field.getName())) {
			return true; // skip field because its name is on the bad variables name
		}

		for (final String badPrefix : badVariablePrefixes) {
			if (field.getName().startsWith(badPrefix)) {
				return true; // skip field because it starts with a prefix that is on the bad prefix
				// list
			}
		}

		return false;
	}
}

class FilteredFieldsCacheStats {
	public static int hits = 0;
	public static int misses = 0;
}