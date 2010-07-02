package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.AbstractEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DtoCopyMachine {
	private static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private static final Set<String> badVariableNames = new HashSet<String>();
	private static final Set<String> badVariablePrefixes = new HashSet<String>();
	private static final DomainClassRegistry registry = DomainClassRegistry.instance;
	
	static {
		badVariablePrefixes.add("$");
		badVariablePrefixes.add("jdo");
		badVariablePrefixes.add("INDEX_");

		badVariableNames.add("serialVersionUID");
	}

	public AbstractEntity copy(Dto dto) {
		final Class<? extends AbstractEntity> entityClass = registry.getDomain(dto.getModule());

		try {
			final AbstractEntity entity = entityClass.newInstance();
			final Field[] allFields = filterFields(reflectionHelper.getAllFields(entityClass));
			
			for (int i=0; i<allFields.length; i++) {
				final Field field = allFields[i];
				
				final Method setter = entityClass.getMethod(reflectionHelper.getMethodName("set", field), field.getType());
				setter.invoke(entity, dto.get(field.getName()));
			}
			
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Dto copy(AbstractEntity entity) {
		final Dto dto = new Dto();
		
		final Class<?> entityClass = entity.getClass();
		final Field[] allFields = filterFields(reflectionHelper.getAllFields(entityClass));
		
		try {
			for (int i=0; i<allFields.length; i++) {
				final Field field = allFields[i];
				
				final Method getter = entityClass.getMethod(reflectionHelper.getMethodName("get", field));
				final Object fieldValue = getter.invoke(entity);
				
				dto.set(field.getName(), fieldValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	
	/**
	 * Method throwing away all fields that are irrelevant and should not be copied by this copy machine (e.g. automatically added fields for serialization).
	 */
	private Field[] filterFields(final Field[] allFields) {
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