package honeycrm.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for all major, reusable reflection related services.
 */
public class ReflectionHelper {
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
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
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
	 * Returns all classes of the specified package that extend the specified super class.
	 */
	public static List<Class<?>> getClassesWithSuperclass(final String packageName, final Class<?> superclass) throws ClassNotFoundException, IOException {
		final List<Class<?>> domainClasses = new ArrayList<Class<?>>();
		for (final Class<?> clazz : getClasses(packageName)) {
			if (superclass.equals(clazz.getSuperclass())) {
				domainClasses.add(clazz);
			}
		}
		return domainClasses;
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
	 * Returns all fields of the source class (classSrc) that are annotated with the given annotation (annotationClass). This is expensive and should be either cached or calculated at startup.
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
	 * Returns an array containing elements of both given arrays array1, array2 i.e. merges the given arrays into one. TODO this is a code duplicate with ArrayHelper.merge(). But cannot replace it with ArrayHelper code since it is situated in client code and Fields cannot be instantiated in client code.
	 */
	private static Field[] merge(final Object[] array1, final Object[] array2) {
		// TODO implement this in a more generic way (for objects in general) but somehow typesafe..
		final Field[] array = new Field[array1.length + array2.length];
		System.arraycopy(array1, 0, array, 0, array1.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);
		return array;
	}

	public static Field[] getFieldsByType(final Field[] fields, final Class<?> type) {
		final ArrayList<Field> filteredFields = new ArrayList<Field>(fields.length);
		for (final Field field : fields) {
			if (field.getType().equals(type)) {
				filteredFields.add(field);
			}
		}
		return filteredFields.toArray(new Field[0]);
	}

	public String getFieldFQN(final Class<?> clazz, final String fieldName) {
		return clazz.toString() + "." + fieldName;
	}
}
