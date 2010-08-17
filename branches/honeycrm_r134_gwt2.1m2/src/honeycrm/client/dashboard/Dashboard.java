package honeycrm.client.dashboard;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Dashboard extends Composite {
	public Dashboard() {
		final FlexTable table = new FlexTable();
		final String[] modules = new String[] { "project", "contact", "donation", "membership" };

		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				final String moduleName = modules[row * 2 + col];
				table.setWidget(row, col, new DashboardListView(moduleName));
			}
		}

		initWidget(new ScrollPanel(table));
	}
}
