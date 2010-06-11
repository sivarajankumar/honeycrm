package honeycrm.client.test;

import java.util.LinkedList;

import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.view.AbstractView.View;

public class FieldEmailCurrencyTest extends AbstractClientTest {
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
}
