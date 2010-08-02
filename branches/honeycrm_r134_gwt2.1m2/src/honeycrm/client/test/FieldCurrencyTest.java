package honeycrm.client.test;

import honeycrm.client.dto.Dto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.TextBox;

public class FieldCurrencyTest extends AbstractClientTest {
	public void testFormatting() {
		final AbstractField field = new FieldCurrency("revenue", "Some label");

		final Dto test = new Dto();
		test.set("1", null);
		test.set("2", Integer.MAX_VALUE);
		test.set("3", Integer.MIN_VALUE);
		test.set("4", Long.MAX_VALUE);
		test.set("5", 0);
		test.set("6", Double.MAX_VALUE);
		test.set("7", Double.MIN_VALUE);
		test.set("8", "foobar");
		test.set("9", new LinkedList<String>());

		for (final String key: test.getAllData().keySet()) {
			field.getWidget(View.DETAIL, test, key);
		}
	}

	public void testGetData() {
		final AbstractField field = new FieldCurrency("income", "Some label");

		final TextBox w = new TextBox();
		w.setText(NumberFormat.getCurrencyFormat("EUR").format(Double.MAX_VALUE));
		assertEquals(Double.MAX_VALUE, field.getData(w));

		w.setText(NumberFormat.getCurrencyFormat("EUR").format(0));
		assertEquals(0.0, field.getData(w));

		for (int i = 0; i < 10; i++) {
			final double d = Random.nextDouble();
			w.setText(NumberFormat.getCurrencyFormat("EUR").format(d));
			// cannot expect an exact match since the random values have a lot more digits than the formatted values (e.g., random value is 0.123 but formatted "0.12")
			assertTrue(Math.abs((Double) field.getData(w) - d) < 0.01);
		}
	}
}
