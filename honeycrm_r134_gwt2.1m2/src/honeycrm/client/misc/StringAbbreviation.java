package honeycrm.client.misc;

public class StringAbbreviation {
	private static final String ABBREVIATION = "..";

	public static String shorten(final String str, final int maxLen) {
		if (null == str || maxLen < 0) {
			return "";
		} else if (str.length() <= maxLen) {
			return str;
		} else if (maxLen <= ABBREVIATION.length()) {
			/**
			 * we cannot make the string that short and add the abbreviation ".."
			 * return a string of the desired size without the indication e.g. shorten("abc", 1) == "a"
			 */
			return str.substring(0, maxLen);
		} else {
			return str.substring(0, maxLen - ABBREVIATION.length()) + ABBREVIATION;
		}
	}
}
