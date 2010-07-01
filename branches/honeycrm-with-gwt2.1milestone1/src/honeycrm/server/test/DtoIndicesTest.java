package honeycrm.server.test;

import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.DemoDataHolder;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

public class DtoIndicesTest extends TestCase {
	private static final Random r = new Random();
	private static final Set<String> skipFields;
	private static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();

	static {
		skipFields = new HashSet<String>();
		skipFields.add("$");
		skipFields.add("serialVersionUID");
	}

	public void testIndicesConsistency() {
		try {
			final Map<Class, Set<String>> map = ReflectionHelper.getFieldNamesOfClassesWithPrefix("honeycrm.client.dto", "Dto");

			for (final Class dto : map.keySet()) {
				final Set<String> fieldNames = map.get(dto);

				existenceCheck(fieldNames);
				uniquenessCheck(dto, fieldNames);
				setterGetterCheck(dto, fieldNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	// Check if the getter and setter operating on the indices return / set the correct values.
	private void setterGetterCheck(final Class dto, final Set<String> fieldNames) throws NoSuchFieldException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		for (final String name : fieldNames) {
			if (!skippedField(name) && !name.startsWith("INDEX_")) {
				final Object dtoInstance = dto.newInstance();

				final Field field = dto.getDeclaredField(name);
				final Field indexField = dto.getDeclaredField("INDEX_" + name.toUpperCase());
				final int indexFieldValue = indexField.getInt(dtoInstance);

				final Method setter = dto.getMethod(reflectionHelper.getMethodName("set", field), field.getType());
				final Method getter = dto.getMethod(reflectionHelper.getMethodName("get", field));
				final Method getFieldValue = dto.getMethod("getFieldValue", int.class);
				final Method setFieldValue = dto.getMethod("setFieldValue", int.class, Object.class);

				// set a random value using the usual setter and check whether both get methods
				// return the same value that has just been set
				final Object value = getRandomValueByType(field.getType());
				setter.invoke(dtoInstance, value);
				assertEquals(value, getter.invoke(dtoInstance));
				assertEquals(value, getFieldValue.invoke(dtoInstance, indexFieldValue));

				// set a new random value using the setFieldValue method and check whether both get
				// methods return the same value that has just been set
				final Object newValue = getRandomValueByType(field.getType());
				setFieldValue.invoke(dtoInstance, indexFieldValue, newValue);

				if (!newValue.equals(getter.invoke(dtoInstance))) {
					System.err.println("equality check failed for field " + name + " / " + indexField.getName());
				}
				assertEquals(newValue, getter.invoke(dtoInstance));
				assertEquals(newValue, getFieldValue.invoke(dtoInstance, indexFieldValue));
			}
		}
	}

	// Check if the indices are locally unique per Dto* class
	private void uniquenessCheck(final Class dto, final Set<String> fieldNames) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
		Set<Integer> indexValues = new HashSet<Integer>();

		for (final String name : fieldNames) {
			if (!skippedField(name) && name.startsWith("INDEX_")) {
				final Field field = dto.getDeclaredField(name);

				// is the INDEX field an integer?
				assertEquals(field.getType(), int.class);

				// is the value of the field unique in the scope of the dto class?
				Integer indexValue = field.getInt(dto.newInstance());
				assertFalse(indexValues.contains(field.get(indexValue)));
				indexValues.add(indexValue);
			}
		}
	}

	// Check if an INDEX_* variable exists for each property of the Dto* class
	private void existenceCheck(final Set<String> fieldNames) {
		for (final String string : fieldNames) {
			if (!skippedField(string) && !string.startsWith("INDEX_")) {
				final String indexFieldName = "INDEX_" + string.toUpperCase();
				if (!fieldNames.contains(indexFieldName)) {
					System.err.println("missing index field " + indexFieldName + " for field " + string);
					fail();
				}
			}
		}
	}

	/**
	 * Returns random values of the specified type (e.g. random integer, long or String values).
	 */
	private Object getRandomValueByType(final Class type) {
		if (int.class == type || Integer.class == type) {
			return r.nextInt();
		} else if (long.class == type || Long.class == type) {
			return r.nextLong();
		} else if (boolean.class == type || Boolean.class == type) {
			return r.nextBoolean();
		} else if (String.class == type) {
			return DemoDataProvider.getRandomString();
		} else if (Date.class == type) {
			return DemoDataHolder.getRandomDate();
		} else if (Double.class == type || double.class == type) {
			return r.nextDouble();
		} else if (List.class == type) {
			return new LinkedList<Object>();
		} else {
			return null;
		}
	}

	private boolean skippedField(String string) {
		for (final String str : skipFields) {
			if (str.equals(string) || string.startsWith(str)) {
				return true;
			}
		}
		return false;
	}
}
