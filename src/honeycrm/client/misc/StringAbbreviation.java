package honeycrm.client.misc;

public class StringAbbreviation {
	private static final String ABBREVIATION = "..";

	public static String shorten(final String str, final int maxLen) {
		if (null == str) {
			return "";
		} else if (str.length() <= maxLen) {
			return str;
		}

		final String shortened = str.substring(0, maxLen - ABBREVIATION.length());

		return shortened + ABBREVIATION;
	}
}
