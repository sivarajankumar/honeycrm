package honeycrm.client.view.list;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.mvp.views.LoadView;

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
	
	public ListViewDataProvider(final String module) {
		this.module = module;
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

		LoadView.get().startLoading();
		// ServiceRegistry.commonService().getAll(module, start, end, new AsyncCallback<ListQueryResult>() {
		ServiceRegistry.readService().getAll(module, start, end, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(final ListQueryResult result) {
				LoadView.get().endLoading();
				insertRefreshedData(display, result);
			}

			@Override
			public void onFailure(final Throwable caught) {
				LoadView.get().endLoading();
				Window.alert("Could not load");
			}
		});
	}

	protected final boolean lastRefreshTooYoung() {
		return System.currentTimeMillis() - lastRefresh < 500;
	}

}
