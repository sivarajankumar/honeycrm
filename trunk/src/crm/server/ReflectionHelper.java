package crm.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import crm.client.dto.AbstractDto;

/**
 * Helper class for all major, reusable reflection related services.
 */
public class ReflectionHelper {
	/**
	 * Traverses a package and returns all classes prefixed with prefix of the package.
	 */
	public static Class[] getClassesWithPrefix(final String packageName, final String prefix) throws ClassNotFoundException, IOException {
		final Class[] classes = getClasses(packageName);
		final List<Class> dtos = new ArrayList<Class>();

		for (Class clazz : classes) {
			if (clazz.getSimpleName().startsWith(prefix)) {
				dtos.add(clazz);
			}
		}

		return dtos.toArray(new Class[] {});
	}

	/**
	 * From http://snippets.dzone.com/posts/show/4831
	 * 
	 * Recursive method used to find all classes in a given directory and subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	/**
	 * From http://snippets.dzone.com/posts/show/4831
	 * 
	 * Scans all classes accessible from the context class loader which belong to the given package
	 * and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Returns the names of all declared fields of the given class.
	 */
	public static Set<String> getFieldNames(final Class clazz) {
		final Set<String> set = new HashSet<String>();

		for (final Field field : clazz.getDeclaredFields()) {
			set.add(field.getName());
		}

		return set;
	}

	/**
	 * Returns a map structure that contains classes and sets of the names of the corresponding
	 * fields. The map contains the classes specified by package name and prefix.
	 */
	public static Map<Class, Set<String>> getFieldNamesOfClassesWithPrefix(final String packageName, final String prefix) throws ClassNotFoundException, IOException {
		final Map<Class, Set<String>> classesWithFieldNames = new HashMap<Class, Set<String>>();
		final Class[] classes = getClassesWithPrefix(packageName, prefix);

		for (final Class clazz : classes) {
			classesWithFieldNames.put(clazz, getFieldNames(clazz));
		}

		return classesWithFieldNames;
	}

	/**
	 * Returns the getter/setter for a property, e.g. getId if fieldName is id
	 */
	public String getMethodName(final String prefix, final Field field) {
		final String firstLetter = field.getName().substring(0, 1);
		final String rest = field.getName().substring(1);

		final boolean isBooleanField = (field.getType() == boolean.class || field.getType() == Boolean.class);

		if ("get".equals(prefix) && isBooleanField) {
			// boolean fields are accessed with is<Variable Name> getters (e.g. isActive()).
			return "is" + firstLetter.toUpperCase() + rest;
		} else {
			return prefix + firstLetter.toUpperCase() + rest;
		}
	}

	/**
	 * Return all fields of the dto class that are from the original domain object, i.e. all
	 * accessible properties except automatically added or those that are only necessary for
	 * organizational purposes.
	 */
	public Field[] getDtoFields(final Class<? extends AbstractDto> dtoClass) {
		final Set<String> badFieldNames = new HashSet<String>();
		badFieldNames.add("serialVersionUID");
		badFieldNames.add("INDEX_");
		badFieldNames.add("$");

		final List<Field> dtoFields = new LinkedList<Field>();
		for (final Field field : dtoClass.getDeclaredFields()) {
			if (!badFieldNames.contains(field.getName()) && !containsPrefix(badFieldNames, field.getName())) {
				dtoFields.add(field);
			}
		}

		return dtoFields.toArray(new Field[0]);
	}

	private static boolean containsPrefix(final Set<String> set, final String str) {
		for (final String prefix : set) {
			if (str.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns fields of classSrc and inherited fields.
	 */
	public Field[] getAllFields(final Class classSrc) {
		final Class superClass = classSrc.getSuperclass();
		final Field[] srcFields = classSrc.getDeclaredFields();

		if (null == superClass) { // does the source class have a super class?
			return srcFields; // no it does not. only iterate over fields of this class.
		} else {
			// yes it has a superclass. we have to copy the superclass fields too.
			return merge(srcFields, getAllFields(superClass));
		}
	}

	/**
	 * Returns all fields annotated with the given annotation
	 */
	public Field[] getAllFieldsWithAnnotation(final Class classSrc, final Class annotationClass) {
		final List<Field> list = new LinkedList<Field>();
		for (final Field field : getAllFields(classSrc)) {
			if (field.isAnnotationPresent(annotationClass)) {
				list.add(field);
			}
		}
		return list.toArray(new Field[0]);
	}

	/**
	 * Returns an array containing elements of both given arrays array1, array2 i.e. merges the
	 * given arrays into one. TODO this is a code duplicate with ArrayHelper.merge(). But cannot
	 * replace it with ArrayHelper code since it is situated in client code and Fields cannot be
	 * instantiated in client code.
	 */
	private static Field[] merge(final Object[] array1, final Object[] array2) {
		// TODO implement this in a more generic way (for objects in general) but somehow typesafe..
		final Field[] array = new Field[array1.length + array2.length];
		System.arraycopy(array1, 0, array, 0, array1.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);
		return array;
	}
}
