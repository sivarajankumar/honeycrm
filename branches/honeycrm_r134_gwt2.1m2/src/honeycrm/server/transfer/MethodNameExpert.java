package honeycrm.server.transfer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Knows method names for getting and settings values. This is encapsulated to allow caching transparently.
 */
// TODO can be deleted since property values can be retrieved via Field.get(Object) instead of Method.invoke(String, Object[])
public class MethodNameExpert {
	public static final MethodNameExpert instance = new MethodNameExpert();
	
	private final Map<Integer, String> getterCache = new HashMap<Integer, String>();
	private final Map<Integer, String> setterCache = new HashMap<Integer, String>();
	
	private MethodNameExpert() {
	}
	
	/**
	 * Get the method name for the given field. Returns a getter name if isGet is true. Returns a setter name otherwise.
	 */
	public String getMethodNameCached(final boolean isGet, final Field field) {
		return getFromCache(isGet ? getterCache : setterCache, isGet, field);
	}

	private String getFromCache(final Map<Integer, String> cache, final boolean isGet, final Field field) {
		final int hashCode = field.hashCode();
		
		if (cache.containsKey(hashCode)) {
			/**
			 * Cache hit: the value can be retrieved from the cache. Hit rate should be > 95%.
			 */
			return cache.get(hashCode);
		} else {
			/**
			 * Cache miss: the value has to be created and put into cache.
			 */
			final String result = getMethodName(isGet, field.getName(), field.getType());
			cache.put(hashCode, result);
			return result;
		}
	}

	private String getMethodName(final boolean isGet, final String fieldName, final Class<?> fieldType) {
		final String firstLetter = fieldName.substring(0, 1);
		final String rest = fieldName.substring(1);

		return getPrefix(isGet, fieldType) + firstLetter.toUpperCase() + rest;
	}

	private String getPrefix(final boolean isGet, final Class<?> type) {
		final boolean isBooleanField = (type == boolean.class || type == Boolean.class);

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
