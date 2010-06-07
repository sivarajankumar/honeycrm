package honeycrm.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionHelper {
	/**
	 * Returns an array containing the elements of a first followed by the elements of b. TODO
	 * implement this in a generic manner.
	 */
	public static int[][] merge(final int[][] a, final int[][] b) {
		final int[][] c = new int[a.length + b.length][];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	/**
	 * Joins all collection items with given glue string.
	 */
	public static String join(final Collection<String> collection, final String glue) {
		String joinedStr = "";

		int i = 0;
		for (final String item : collection) {
			joinedStr += item;

			if (i++ < collection.size() - 1) {
				joinedStr += glue;
			}
		}

		return joinedStr;
	}

	/**
	 * Convert a string array into a set.
	 */
	public static Set<String> toSet(final String[] array) {
		final Set<String> set = new HashSet<String>();
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				set.add(array[i]);
			}
		}
		return set;
	}
}
