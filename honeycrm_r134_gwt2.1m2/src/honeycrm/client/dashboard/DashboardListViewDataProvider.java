package honeycrm.client.dashboard;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.login.User;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.view.list.ListViewDataProvider;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class DashboardListViewDataProvider extends ListViewDataProvider {
	public DashboardListViewDataProvider(final String module, final ReadServiceAsync readService) {
		super(module, readService);
	}
	
	@Override
	public void refresh(final HasData<Dto> display) {
		if (lastRefreshTooYoung())
			return;
		
		lastRefresh = System.currentTimeMillis();
		
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int end = start + range.getLength();
		
		readService.getAllAssignedTo(module, User.getUserId(), start, end, new AsyncCallback<ListQueryResult>() {
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
