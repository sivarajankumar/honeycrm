package crm.server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

public class CopyMachine {
	private static final Set<String> skipPrefixes = new HashSet<String>();

	static {
		skipPrefixes.add("$");
		skipPrefixes.add("jdo");
		skipPrefixes.add("INDEX_");
	}

	/**
	 * Copy all fields from source into equally named fields of the destination class. Returns an instance of the destination class with the values of the source object set.
	 */
	public Object copy(final Object srcObj, final Class destClass) {
		assert null != srcObj && null != destClass;

		try {
			final Class srcClass = srcObj.getClass();
			final Object destInstance = destClass.newInstance();

			copyFields(srcObj, srcClass, destClass, destInstance);

			return destInstance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a new instance of the destObject class with the old contents of destObjects, overwritten with the fields existing in srcObject.
	 * 
	 * @param srcObject
	 *            e.g. DTO object
	 * @param destObject
	 *            e.g. DB object.
	 * @return A new instance of the destObjects class. This is a copy of destObject, but all fields that exist in srcObject too are copied from srcObject into the new instance. This fields from srcObject will overwrite corresponding fields in new instance.
	 */
	public Object getUpdatedInstance(final Object srcObject, final Object destObject) {
		assert null != srcObject && null != destObject;

		try {
			final Class classSrc = srcObject.getClass();
			final Class classDest = destObject.getClass();
			// Copy all values from the destObject into the updated instance.
			final Object instanceUpdatedDest = copy(destObject, classDest);

			// Now we will overwrite the values of all fields of instanceUpdatedDest with those from srcObject.
			// TODO Key is not copied from old DB class to new instance
			copyFields(srcObject, classSrc, classDest, instanceUpdatedDest);

			return instanceUpdatedDest;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copies all fields from the source object into the destination object.
	 * 
	 * @param srcObject
	 * @param classSrc
	 * @param classDest
	 * @param instanceUpdatedDest
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void copyFields(final Object srcObject, final Class classSrc, final Class classDest, final Object instanceUpdatedDest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Field[] srcFields = classSrc.getDeclaredFields();

		for (int i = 0; i < srcFields.length; i++) {
			final Field srcField = srcFields[i];
			final String srcFieldName = srcField.getName();

			if (shouldBeSkipped(srcFieldName)) {
				continue;
			}

			// assumption: if we reach this point the get method for the field has to exist.

			// we call the get method for this field on the source object to avoid an illegal access exception that could occur if the property is private
			final Method getter = classSrc.getMethod(ReflectionHelper.getMethodName("get", srcField));
			final Object srcFieldValue = getter.invoke(srcObject);

			if (null != srcFieldValue) { // only call the setter if the field has any value.
				try {
					// call the set method for this field on the destination object
					final Method setter = classDest.getMethod(ReflectionHelper.getMethodName("set", srcField), srcField.getType());
					setter.invoke(instanceUpdatedDest, srcFieldValue);
				} catch (NoSuchMethodException e) {
					// Assume this happened because we tried to set the id (class Key) of a DB class
					// on a DTO class that only has a long field for storing the id. Handle this as
					// a special case here.
					if ("id".equals(srcFieldName) || srcFieldName.endsWith("ID")) {
						if (srcFieldValue instanceof Long) {
							// TODO cannot copy long id value into key since we cannot intantiate keys..
							// TODO try Key k = KeyFactory.createKey(Employee.class.getSimpleName(), "Alfred.Smith@example.com");
						} else if (srcFieldValue instanceof Key) {
							// The id field is a Key instance in the server code.
							// Since we cannot use this class in client code it is represented as a string in the client code
							// For this reason handle this field as a special case.
							final Method setter = classDest.getMethod(ReflectionHelper.getMethodName("set", srcField), long.class);
							setter.invoke(instanceUpdatedDest, ((Key) srcFieldValue).getId());
						}
					} else {
						// will not invoke the setter because field does not exist in the destination class.
						// log error?
					}
				}
			}
		}
	}

	/**
	 * Returns true if the field with the given name should be skipped during copying / DTO-DB comparison. False otherwise.
	 */
	public boolean shouldBeSkipped(final String srcFieldName) {
		if (srcFieldName.equals("serialVersionUID")) {
			return true;
		}
		for (final String badPrefix : skipPrefixes) {
			if (srcFieldName.startsWith(badPrefix)) {
				return true;
			}
		}
		return false;
	}

	public Set<String> getSkipPrefixes() {
		return skipPrefixes;
	}
}
