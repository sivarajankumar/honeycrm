package honeycrm.server.test;

import java.util.Random;

import honeycrm.client.misc.NumberParser;
import junit.framework.TestCase;

public class NumberParserTest extends TestCase {
	public void testParseDouble() {
		assertEquals(0.0, NumberParser.convertToDouble(null));
		assertEquals(Double.MIN_VALUE, NumberParser.convertToDouble(Double.MIN_VALUE));

		double d = new Random().nextDouble();
		assertEquals(d, NumberParser.convertToDouble(String.valueOf(d)));
	}
}
