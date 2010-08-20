package honeycrm.server.test;

import honeycrm.client.misc.StringAbbreviation;
import honeycrm.server.DemoDataProvider;

import java.util.Random;

import junit.framework.TestCase;

public class StringAbbreviationTest extends TestCase {
	private final Random r = new Random(System.currentTimeMillis());

	public void testAbbreviation() {
		for (int i = 0; i < 100; i++) {
			final int maxLen = r.nextInt(200);
			final String str = DemoDataProvider.getRandomString();
			final String shortened = StringAbbreviation.shorten(str, maxLen);
			assertTrue(shortened.length() <= maxLen);
		}
	}

	public void testNull() {
		assertEquals("", StringAbbreviation.shorten(null, Integer.MIN_VALUE));
	}
}
