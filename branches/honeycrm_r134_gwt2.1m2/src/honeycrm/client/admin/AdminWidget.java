package honeycrm.client.admin;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminWidget extends Composite {
	public AdminWidget() {
		final Panel table = new VerticalPanel();

		for (final Composite widget : new Composite[] { new LocaleSettingsWidget(), new DatabaseWidget(), new CacheStatsWidget(), LogConsole.get() }) {
			table.add(widget);
		}

		initWidget(new ScrollPanel(table));
	}
}
