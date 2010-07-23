package honeycrm.server.test;

import java.lang.reflect.Field;

import junit.framework.TestCase;

public class DtoSyncTest extends TestCase {
	/**
	 * Returns true if both arrays contain the same fields.
	 */
	private boolean areEqualSets(Field[] fields, Field[] fields2) {
		return isSubsetOf(fields, fields2) && isSubsetOf(fields2, fields);
	}

	/**
	 * Returns true if fields contains a field called fieldName TODO check field type as well. not just the name..
	 */
	private boolean containsField(final String fieldName, Field[] fields) {
		for (int k = 0; k < fields.length; k++) {
			if (fieldName.equals(fields[k].getName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if all values of fields are in fields2 as well.
	 */
	private boolean isSubsetOf(Field[] fields, Field[] fields2) {
		for (int i = 0; i < fields.length; i++) {
			final String name = fields[i].getName();

			if (name.equals("id") || name.equals("serialVersionUID") || name.startsWith("jdo")) {
				continue; // skip these fields
			}

			if (!containsField(name, fields2)) {
				System.err.println("missing field '" + name + "'");
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines whether the domain classes and the dtos are in sync, i.e. whether all DTO fields can be found in the domain class and vice versa.
	 */
	public void testSync() {
		// TODO

		/*
		 * try { final CopyMachine copy = new CopyMachine();
		 * 
		 * Class[] domainClasses = ReflectionHelper.getClasses("honeycrm.server.domain"); Class[] dtos = ReflectionHelper.getClasses("honeycrm.client.dto");
		 * 
		 * boolean outOfSync = false;
		 * 
		 * for (int i = 0; i < domainClasses.length; i++) { final Class domain = domainClasses[i]; final String domainName = domain.getSimpleName();
		 * 
		 * for (int k = 0; k < dtos.length; k++) { final Class dto = dtos[k]; final String dtoName = dto.getSimpleName().substring(3); // throw away the first // three characters. // this should skip // the "Dto" at the // beginning
		 * 
		 * if (dtoName.equals(domainName)) { final List<Field> dtoFields = new LinkedList<Field>();
		 * 
		 * for (final Field field : dto.getDeclaredFields()) { if (!copy.shouldBeSkipped(field)) { dtoFields.add(field); } }
		 * 
		 * final List<Field> dbFields = new LinkedList<Field>(); for (final Field field : domain.getDeclaredFields()) { if (!copy.shouldBeSkipped(field)) { dbFields.add(field); } }
		 * 
		 * if (!areEqualSets(dtoFields.toArray(new Field[] {}), dbFields.toArray(new Field[] {}))) { System.out.println(dto.getName() + " -> " + domain.getName() + " is out of sync"); outOfSync = true; } } } }
		 * 
		 * if (outOfSync) { fail(); }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); fail(); }
		 */
	}

}
