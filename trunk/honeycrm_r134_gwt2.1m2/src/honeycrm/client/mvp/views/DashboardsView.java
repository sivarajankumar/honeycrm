package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.DashboardsPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class DashboardsView extends Composite implements Display {
	private static final int COLS = 2;
	private static DashboardsViewUiBinder uiBinder = GWT.create(DashboardsViewUiBinder.class);
	private final HashMap<String, DashboardView> views = new HashMap<String, DashboardView>();

	interface DashboardsViewUiBinder extends UiBinder<Widget, DashboardsView> {
	}

	@UiField
	Button refreshBtn;
	@UiField
	FlexTable table;
	private final ReadServiceAsync readService;
	private final LocalizedMessages constants;

	public DashboardsView(final ReadServiceAsync readService, final LocalizedMessages constants) {
		this.readService = readService;
		this.constants = constants;
		
		initWidget(uiBinder.createAndBindUi(this));

		refreshBtn.setText("Refresh");
	}

	@Override
	public HasClickHandlers getRefreshBtn() {
		return refreshBtn;
	}

	@Override
	public void setDashboardModules(ArrayList<String> modules) {
		setupViews(modules);

		for (int row = 0; row < modules.size() / COLS; row++) {
			for (int col = 0; col < COLS; col++) {
				table.setWidget(row, col, views.get(modules.get(row * COLS + col)));
			}
		}
	}

	private void setupViews(ArrayList<String> modules) {
		for (int i = 0; i < modules.size(); i++) {
			views.put(modules.get(i), new DashboardView(modules.get(i), readService, constants));
		}
	}

	@Override
	public void insertRefreshedData(HashMap<String, ListQueryResult> result) {
		for (final Map.Entry<String, ListQueryResult> entry : result.entrySet()) {
			if (views.containsKey(entry.getKey())) {
				views.get(entry.getKey()).insertRefreshedData(entry.getValue());
			}
		}
	}

	@Override
	public Collection<DashboardView> getDashboardViews() {
		return views.values();
	}
}
