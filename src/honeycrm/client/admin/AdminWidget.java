package honeycrm.client.admin;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class AdminWidget extends Composite {
	public AdminWidget() {
		final FlexTable table = new FlexTable();

		int row = 0;
		for (final Composite widget : new Composite[] { new LocaleSettingsWidget(), new DatabaseWidget(), LogConsole.get() }) {
			table.setWidget(row++, 0, widget);
		}

		initWidget(table);
	}
}
