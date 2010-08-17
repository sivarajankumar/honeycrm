package honeycrm.client.dashboard;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.login.User;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.view.ListView;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DashboardListView extends ListView {
	public DashboardListView(String module) {
		super(module);

		setShowTitle(true);
		setPageSize(20);
		setAllowDelete(false);
		
		refresh();
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
