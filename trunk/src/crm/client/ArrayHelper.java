package crm.client;

import java.util.List;

public class ArrayHelper {
	/**
	 * Returns an array containing the elements of a first followed by the elements of b.
	 * TODO implement this in a generic manner.
	 */
	public static int[][] merge(final int[][] a, final int[][] b) {
		final int[][] c = new int[a.length + b.length][];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	/**
	 * Joins all list items with given glue string.
	 */
	public static String join(final List<String> list, final String glue) {
		String str = "";

		for (int i = 0; i < list.size(); i++) {
			str += list.get(i);

			if (i < list.size() - 1) {
				str += glue;
			}
		}

		return str;
	}
}
