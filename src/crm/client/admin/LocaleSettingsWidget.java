package crm.client.admin;

import com.google.gwt.i18n.client.CurrencyData;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class LocaleSettingsWidget extends Composite {
	private final ListBox currencySelector = getCurrencyBox();
	private final ListBox dateFormatSelector = getDateFormatBox();
	
	public LocaleSettingsWidget() {
		final FlexTable table = new FlexTable();
		
		table.setWidget(0, 0, new Label("Date format"));
		table.setWidget(0, 1, dateFormatSelector);
		table.setWidget(1, 0, new Label("Currency"));
		table.setWidget(1, 1, currencySelector);
		
		initWidget(table);
	}

	private ListBox getDateFormatBox() {
		final ListBox box = new ListBox();
		
		box.addItem(DateTimeFormat.getMediumDateFormat().getPattern());
		
		return box;
	}

	private ListBox getCurrencyBox() {
		final ListBox box = new ListBox();

		for (final CurrencyData currency : CurrencyList.get()) {
			final String label = currency.getCurrencyCode() + " " + currency.getCurrencySymbol();
			final String value = currency.getCurrencyCode();

			box.addItem(label, value);
		}

		return box;
	}
}
