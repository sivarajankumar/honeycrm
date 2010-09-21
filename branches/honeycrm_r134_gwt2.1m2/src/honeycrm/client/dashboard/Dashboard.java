package honeycrm.client.dashboard;

import honeycrm.client.misc.WidgetJuggler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends Composite {
	private static final int COLS = 2;
	private static final String[] modules = new String[] { "project", "contact", "donation", "membership", "offering", "contract", "account" };

	public Dashboard() {
		final VerticalPanel panel = new VerticalPanel();
		initWidget(new ScrollPanel(panel));

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				final DashboardListView[] listViews = getListViews();
				panel.add(getRefreshButton(listViews));
				panel.add(getTable(listViews));
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
	}

	private DashboardListView[] getListViews() {
		DashboardListView[] listViews = new DashboardListView[modules.length];
		for (int i = 0; i < modules.length; i++) {
			listViews[i] = new DashboardListView(modules[i]);
		}
		return listViews;
	}

	private Widget getRefreshButton(final DashboardListView[] listViews) {
		return WidgetJuggler.getButton("Refresh", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for (final DashboardListView listview : listViews) {
					listview.refresh();
				}
			}
		});
	}

	private Widget getTable(final DashboardListView[] listViews) {
		final FlexTable table = new FlexTable();
		for (int row = 0; row < modules.length / COLS; row++) {
			for (int col = 0; col < COLS; col++) {
				table.setWidget(row, col, listViews[row * COLS + col]);
			}
		}
		return table;
	}
}
