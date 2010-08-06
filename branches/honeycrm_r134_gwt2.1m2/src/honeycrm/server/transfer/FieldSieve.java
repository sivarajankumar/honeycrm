package honeycrm.server.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldSieve {
	public static final FieldSieve instance = new FieldSieve();
	
	private final Set<String> badVariableNames = new HashSet<String>();
	private final Set<String> badVariablePrefixes = new HashSet<String>();
	private static final Map<Integer, Field[]> cache = new HashMap<Integer, Field[]>();

	private FieldSieve() {
		badVariablePrefixes.add("$");
		badVariablePrefixes.add("jdo");
		badVariablePrefixes.add("INDEX_");
		badVariableNames.add("serialVersionUID");
	}
	
	/**
	 * Method throwing away all fields that are irrelevant and should not be copied by this copy machine (e.g. automatically added fields for serialization).
	 */
	public Field[] filterFields(final Field[] allFields) {
		final int hashCode = allFields.hashCode();
		
		if (cache.containsKey(hashCode)) {
			return cache.get(hashCode);
		} else {
			final Field[] fields = getFilteredFields(allFields);
			cache.put(hashCode, fields);
			return fields;
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
