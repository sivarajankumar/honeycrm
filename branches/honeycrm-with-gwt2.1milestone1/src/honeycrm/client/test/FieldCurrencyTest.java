package honeycrm.client.test;

import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.view.AbstractView.View;

import java.util.LinkedList;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.TextBox;

public class FieldCurrencyTest extends AbstractClientTest {
	public void testFormatting() {
		final AbstractField field = new FieldCurrency(1, "Some label");

		field.getWidget(View.DETAIL, null);
		field.getWidget(View.DETAIL, Integer.MAX_VALUE);
		field.getWidget(View.DETAIL, Integer.MIN_VALUE);
		field.getWidget(View.DETAIL, Long.MAX_VALUE);
		field.getWidget(View.DETAIL, 0);
		field.getWidget(View.DETAIL, Double.MAX_VALUE);
		field.getWidget(View.DETAIL, Double.MIN_VALUE);
		field.getWidget(View.DETAIL, "foobar");
		field.getWidget(View.DETAIL, new LinkedList<String>());
	}

	public void testGetData() {
		final AbstractField field = new FieldCurrency(1, "Some label");

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
