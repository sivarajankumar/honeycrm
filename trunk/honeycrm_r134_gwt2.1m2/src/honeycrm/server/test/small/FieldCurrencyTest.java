package honeycrm.server.test.small;

import honeycrm.client.dto.Dto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldCurrency;

import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

// TODO rewrite formatting tests without requiring client side GWT.create() call caused by NumberFormat/Random class.
public class FieldCurrencyTest extends TestCase {
	public void testFormatting() {
		final AbstractField field = new FieldCurrency("revenue", "Some label", "0");

		final Dto test = new Dto();
		test.set("1", 0);
		test.set("2", Integer.MAX_VALUE);
		test.set("3", Integer.MIN_VALUE);
		test.set("4", Long.MAX_VALUE);
		test.set("5", 0);
		test.set("6", Double.MAX_VALUE);
		test.set("7", Double.MIN_VALUE);
		test.set("8", "foobar");
		test.set("9", new ArrayList<String>());

		/*
		 * for (final String key: test.getAllData().keySet()) { field.getWidget(View.DETAIL, test, key); }
		 */
	}

	public void testGetData() {
		final AbstractField field = new FieldCurrency("income", "Some label", "0");
		// final String formattedString = NumberFormat.getCurrencyFormat("EUR").format(Double.MAX_VALUE);
		// assertEquals(Double.MAX_VALUE, field.getData(formattedString));
	}

	public void testFormat0() {
		// assertEquals("0", NumberFormat.getCurrencyFormat("EUR").format(0));
		// assertEquals(0.0, field.getData(w));
	}

	public void testDoubleFormatting() {
		final Random r = new Random();
		for (int i = 0; i < 10; i++) {
			final double d = r.nextDouble();
			// final String formattedString = NumberFormat.getCurrencyFormat("EUR").format(d);
			// cannot expect an exact match since the random values have a lot more digits than the formatted values (e.g., random value is 0.123 but formatted "0.12")
			// assertTrue(Math.abs((Double) field.getData(w) - d) < 0.01);
		}
	}
}
