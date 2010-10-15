package honeycrm.client.misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionHelper {
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
	
	public static String join(final String glue, final Object... array) {
		return join(toStringList(array), glue);
	}

	public static ArrayList<Serializable> toList(final Serializable... array) {
		final ArrayList<Serializable> list = new ArrayList<Serializable>(array.length);
		for (final Serializable item: array) {
			list.add(item);
		}
		return list;
	}
	
	private static List<String> toStringList(final Object... array) {
		final List<String> list = new ArrayList<String>();
		for (final Object item: array) {
			list.add(String.valueOf(item));
		}
		return list;
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
	
	/**
	 * Convert an arbitrary collection (e.g. set) into a list. This is necessary for sorting collections for example. Only lists can be sorted.
	 */
	public static List<String> toList(final Collection<String> set) {
		return new ArrayList<String>(set);
	}
}
