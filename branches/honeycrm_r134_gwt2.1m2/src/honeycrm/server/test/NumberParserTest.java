package honeycrm.server.test;

import honeycrm.client.misc.NumberParser;

import java.util.Random;

import junit.framework.TestCase;

public class NumberParserTest extends TestCase {
	private final Random r = new Random(System.currentTimeMillis());

	public void testParseDouble() {
		assertEquals(0.0, NumberParser.convertToDouble(null));
		assertEquals(0.0, NumberParser.convertToDouble("foo"));
		assertEquals(Double.MIN_VALUE, NumberParser.convertToDouble(Double.MIN_VALUE));

		final double d = r.nextDouble();
		assertEquals(d, NumberParser.convertToDouble(String.valueOf(d)));
	}

	public void testParseLong() {
		assertEquals(0, NumberParser.convertToLong(null));
		assertEquals(0, NumberParser.convertToLong("foo"));
		assertEquals(Long.MIN_VALUE, NumberParser.convertToLong(String.valueOf(Long.MIN_VALUE)));
		assertEquals(Long.MAX_VALUE, NumberParser.convertToLong(Long.MAX_VALUE));

		final long i = r.nextLong();
		assertEquals(i, NumberParser.convertToLong(String.valueOf(i)));
	}

	public void testParseInt() {
		assertEquals(0, NumberParser.convertToInteger(null));
		assertEquals(0, NumberParser.convertToInteger("foo"));
		assertEquals(Integer.MIN_VALUE, NumberParser.convertToInteger(String.valueOf(Integer.MIN_VALUE)));
		assertEquals(0, NumberParser.convertToInteger(Long.MAX_VALUE));

		final int i = r.nextInt();
		assertEquals(i, NumberParser.convertToInteger(String.valueOf(i)));
		assertEquals(i, NumberParser.convertToInteger(i));
	}
}
