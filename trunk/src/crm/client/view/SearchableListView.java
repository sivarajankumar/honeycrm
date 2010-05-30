package crm.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;

import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

public class SearchableListView extends PaginatingListView {
	private AbstractDto searchViewable;

	public SearchableListView(Class<? extends AbstractDto> clazz) {
		super(clazz);
	}

	public void search(final AbstractDto searchedViewable) {
		this.searchViewable = searchedViewable;
		doSearchForPage(1);
	}

	private void doSearchForPage(final int page) {
		LoadIndicator.get().startLoading();

		commonService.search(IANA.mashal(clazz), searchViewable, getOffsetForPage(page), getOffsetForPage(page) + MAX_ENTRIES, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				insertSearchResults(page, result);
			}
		});
	}

	public void clearSearch() {
		refresh();
	}

	public void getAllMarked() {
		doMarkedSearchForPage(1);
		// TODO create a new fresh viewable instance, then only set martked to true
		// TODO actually we must not overwrite the current searchViewable variable
//		this.searchViewable.setMarked(true);
//		search(searchViewable);
	}

	private void doMarkedSearchForPage(final int page) {
		LoadIndicator.get().startLoading();

		commonService.getAllMarked(IANA.mashal(clazz), getOffsetForPage(page), getOffsetForPage(page)+MAX_ENTRIES, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				insertSearchResults(page, result);
			}
		});
	}

	private void insertSearchResults(final int page, ListQueryResult<? extends AbstractDto> result) {
		LoadIndicator.get().endLoading();

		// TODO
		cache.put(page, result.getResults());
		setNumberOfPages(result.getItemCount());
		showPage(page);
	}

	@Override
	public void showPageRight() {
		doSearchForPage(currentPage + 1);
	}
}
