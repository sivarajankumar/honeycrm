package honeycrm.client.view.list;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.Callback;
import honeycrm.client.services.ReadServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ListViewDataProvider extends AsyncDataProvider<Dto> {
	protected final String module;
	// This setting simulates that at creation time a refresh has occurred.
	// Setting this to the current time to avoid a refresh at startup.
	// protected long lastRefresh = System.currentTimeMillis();
	protected long lastRefresh = 0;
	protected final ReadServiceAsync readService;
	
	public ListViewDataProvider(final String module, final ReadServiceAsync readService) {
		this.module = module;
		this.readService = readService;
	}

	@Override
	protected void onRangeChanged(final HasData<Dto> display) {
		refresh(display);
	}

	public void insertRefreshedData(final HasData<Dto> display, final ListQueryResult result) {
		lastRefresh = System.currentTimeMillis();

		final List<Dto> list = new ArrayList<Dto>();

		if (null != result && null != result.getResults()) {
			for (final Dto dto : result.getResults()) {
				list.add(dto);
			}
		}

		display.setRowCount(result == null ? 0 : result.getItemCount(), true);
		display.setRowData(display.getVisibleRange().getStart(), list);
	}

	public void refresh(final HasData<Dto> display) {
		if (lastRefreshTooYoung())
			return;

		lastRefresh = System.currentTimeMillis();

		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int end = start + range.getLength();

		// ServiceRegistry.commonService().getAll(module, start, end, new AsyncCallback<ListQueryResult>() {
		readService.getAll(module, start, end, new AsyncCallback<ListQueryResult>() {
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

	protected final boolean lastRefreshTooYoung() {
		return System.currentTimeMillis() - lastRefresh < 500;
	}

	public void getList(final Callback<ArrayList<Dto>> callback) {
		readService.getAll(module, 0, 9999, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(ListQueryResult result) {
				ArrayList<Dto> list = new ArrayList<Dto>();
				
				for (Dto d: result.getResults()) {
					list.add(d);
				}
				
				callback.callback(list);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
}
