package crm.server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.Key;

public class CopyMachine {
	private static final Set<String> badVariableNames = new HashSet<String>();
	private static final Set<String> badVariablePrefixes = new HashSet<String>();

	static {
		badVariablePrefixes.add("$");
		badVariablePrefixes.add("jdo");
		badVariablePrefixes.add("INDEX_");
		
		badVariableNames.add("serialVersionUID");
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
	 */
	private void copyFields(final Object srcObject, final Class classSrc, final Class classDest, final Object instanceUpdatedDest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Field[] allFields = filterFields(ReflectionHelper.getAllFields(classSrc));

		for (int i = 0; i < allFields.length; i++) {
			final Field srcField = allFields[i];
			final String srcFieldName = srcField.getName();

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
	 * Method throwing away all fields that are irrellevant and should not be copied by this copy machine (e.g. automatically added fields for serialization).
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
	public boolean shouldBeSkipped(final Field field) {
		if (Modifier.STATIC == (field.getModifiers() & Modifier.STATIC)) {
			return true; // skip static fields
		}
		
		if (badVariableNames.contains(field.getName())) {
			return true; // skip field because its name is on the bad variables name
		}

		for (final String badPrefix : badVariablePrefixes) {
			if (field.getName().startsWith(badPrefix)) {
				return true; // skip field because it starts with a prefix that is on the bad prefix list
			}
		}
		
		return false;
	}

	public Set<String> getSkipPrefixes() {
		return badVariablePrefixes;
	}
}
