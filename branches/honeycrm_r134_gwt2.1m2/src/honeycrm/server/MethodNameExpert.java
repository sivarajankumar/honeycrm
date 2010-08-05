package honeycrm.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MethodNameExpert {
	private static final MethodNameExpert instance = new MethodNameExpert();
	
	private final Map<Integer, String> getMethodNameCache = new HashMap<Integer, String>();
	private final Map<Integer, String> setMethodNameCache = new HashMap<Integer, String>();
	
	private int misses = 0;
	private int hits = 0;
	
	private MethodNameExpert() {
	}
	
	public static MethodNameExpert get() {
		return instance;
	}
	
	public String getMethodNameCached(final boolean isGet, final Field field) {
		final int hashCode = field.hashCode();

		if (isGet) {
			if (getMethodNameCache.containsKey(hashCode)) {
				hits++;
				return getMethodNameCache.get(hashCode);
			} else {
				misses++;
				final String result = getMethodName(isGet, field);
				getMethodNameCache.put(hashCode, result);
				return result;
			}
		} else {
			if (setMethodNameCache.containsKey(hashCode)) {
				hits++;
				return setMethodNameCache.get(hashCode);
			} else {
				misses++;
				final String result = getMethodName(isGet, field);
				setMethodNameCache.put(hashCode, result);
				return result;
			}
		}
	}

	private String getMethodName(final boolean isGet, final Field field) {
		final String firstLetter = field.getName().substring(0, 1);
		final String rest = field.getName().substring(1);

		return getPrefix(isGet, field) + firstLetter.toUpperCase() + rest;
	}

	private String getPrefix(final boolean isGet, final Field field) {
		final boolean isBooleanField = (field.getType() == boolean.class || field.getType() == Boolean.class);

		if (isGet) {
			if (isBooleanField) {
				return "is"; // boolean fields are accessed with is<Variable Name> getters (e.g. isActive()).
			} else {
				return "get";
			}
		} else {
			return "set";
		}
	}
}
