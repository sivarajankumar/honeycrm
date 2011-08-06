package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.Callback;
import honeycrm.client.mvp.events.RpcBeginEvent;
import honeycrm.client.mvp.events.RpcEndEvent;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.view.list.ListViewDataProvider;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ContactsDataProvider extends ListViewDataProvider {
	private SimpleEventBus bus;

	public ContactsDataProvider(final String module, SimpleEventBus bus) {
		super(module, null);
		this.bus = bus;
	}
	
	@Override
	protected void onRangeChanged(HasData<Dto> display) {
		refresh(display, null);
	}

	public void refresh(final HasData<Dto> display, ColumnSortList columnSortList) {
		if (lastRefreshTooYoung())
			return;

		lastRefresh = System.currentTimeMillis();

		final Range range = display.getVisibleRange();
		final int start = range.getStart();
		final int end = start + range.getLength();
		final String sortCol = "name";
		final SortDirection direction = (columnSortList == null || columnSortList.size() == 0) ? SortDirection.Ascending : (columnSortList.get(0).isAscending() ? SortDirection.Ascending : SortDirection.Descending);

		bus.fireEvent(new RpcBeginEvent());
		
		AsyncProvider.getReadService(new Callback<ReadServiceAsync>() {
			@Override
			public void callback(ReadServiceAsync arg) {
				arg.getAll(module, sortCol, direction, start, end, new AsyncCallback<ListQueryResult>() {
					@Override
					public void onSuccess(ListQueryResult result) {
						bus.fireEvent(new RpcEndEvent());
						insertRefreshedData(display, result);
					}

					@Override
					public void onFailure(Throwable caught) {
						bus.fireEvent(new RpcEndEvent());
						Window.alert("Could not load");
					}
				});
			}
		});
	}

	public void search(final String search, final HasData<Dto> display, final ColumnSortList columnSortList) {
		if (search.isEmpty()) {
			refresh(display, columnSortList);
		} else {
			final Range range = display.getVisibleRange();
			final int start = range.getStart();
			final int end = start + range.getLength();
			
			bus.fireEvent(new RpcBeginEvent());
			
			AsyncProvider.getReadService(new Callback<ReadServiceAsync>() {
				@Override
				public void callback(ReadServiceAsync arg) {
					arg.fulltextSearchForModule("Contact", search, start, end, new AsyncCallback<ListQueryResult>() {
						@Override
						public void onSuccess(ListQueryResult result) {
							bus.fireEvent(new RpcEndEvent());
							insertRefreshedData(display, result);
						}
		
						@Override
						public void onFailure(Throwable caught) {
							bus.fireEvent(new RpcEndEvent());
						}
					});
				}
			});
		}
	}
}