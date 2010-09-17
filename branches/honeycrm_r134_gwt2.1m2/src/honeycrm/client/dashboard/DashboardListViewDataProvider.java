package honeycrm.client.dashboard;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.login.User;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.view.list.ListViewDataProvider;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class DashboardListViewDataProvider extends ListViewDataProvider {
	public DashboardListViewDataProvider(final String module) {
		super(module);
	}
	
	@Override
	public void refresh(final HasData<Dto> display) {
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int end = start + range.getLength();
		
		ServiceRegistry.commonService().getAllAssignedTo(module, User.getUserId(), start, end, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(ListQueryResult result) {
				insertRefreshedData(display, result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not load");
			}
		});

	}

}
