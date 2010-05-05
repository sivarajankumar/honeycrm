package crm.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

public class SearchableListView extends ListView {
	public SearchableListView(Class<? extends AbstractDto> clazz) {
		super(clazz);
	}

	public void search(Viewable tmpViewable) {
		LoadIndicator.get().startLoading();

		commonService.search(IANA.mashal(clazz), viewable, 0, MAX_ENTRIES, new AsyncCallback<ListQueryResult<? extends Viewable>>() {
			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
				Window.alert("search failed");
			}

			@Override
			public void onSuccess(ListQueryResult<? extends Viewable> result) {
				LoadIndicator.get().endLoading();

				// TODO 
				cache.put(currentPage(), result.getResults());
				setNumberOfPages(result.getItemCount());
				showPage(currentPage());
			}
		});
	}

	public void clearSearch() {
		refresh();
	}
}
