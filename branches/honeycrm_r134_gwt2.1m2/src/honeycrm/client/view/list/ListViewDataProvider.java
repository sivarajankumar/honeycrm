package honeycrm.client.view.list;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ListViewDataProvider extends AsyncDataProvider<Dto> {
	protected final String module;

	public ListViewDataProvider(final String module) {
		this.module = module;
	}

	@Override
	protected void onRangeChanged(final HasData<Dto> display) {
		refresh(display);
	}

	protected void insertRefreshedData(final HasData<Dto> display, final ListQueryResult result) {
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
		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int end = start + range.getLength();

		ServiceRegistry.commonService().getAll(module, start, end, new AsyncCallback<ListQueryResult>() {
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
