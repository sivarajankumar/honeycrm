package honeycrm.client.dashboard;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.login.User;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.view.ListView;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
				History.newItem(HistoryTokenFactory.get(moduleDto.getModule(), ModuleAction.CREATE));
			}
		});
	}

	@Override
	protected void refreshPage(int page) {
		ServiceRegistry.commonService().getAllAssignedTo(moduleDto.getModule(), User.getUserId(), 0, 20, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(ListQueryResult result) {
				refreshListViewValues(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("cannot execute getAllAssignedTo('" + moduleDto.getModule() + "'," + User.getUserId() + ")");
			}
		});
	}
	
	@Override
	protected String getListTitle() {
		return "My " + super.getListTitle();
	}
}
