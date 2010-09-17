package honeycrm.client.dashboard;

import honeycrm.client.login.User;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.view.ModuleAction;
import honeycrm.client.view.list.ListView;
import honeycrm.client.view.list.ListViewDataProvider;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;

public class DashboardListView extends ListView {
	public DashboardListView(String module) {
		super(module);

		setShowTitle(true);
		setPageSize(20);
		setAllowDelete(false);
		
		setAdditionalButtons(getCreateButton());
		
		refresh();
	}

	private Button getCreateButton() {
		return WidgetJuggler.getButton("Create", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(HistoryTokenFactory.get(moduleDto.getModule(), ModuleAction.CREATE, "assignedTo", User.getUserId()));
			}
		});
	}

	@Override
	protected ListViewDataProvider getListDataProvider() {
		return new DashboardListViewDataProvider(moduleDto.getModule());
	}
	
	@Override
	protected String getListTitle() {
		return "My " + super.getListTitle();
	}
}
