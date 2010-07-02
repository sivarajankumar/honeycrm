package honeycrm.client.view;

import honeycrm.client.LoadIndicator;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class SearchableListView extends PaginatingListView {
	private Dto searchViewable;

	public SearchableListView(final Dto clazz) {
		super(clazz);
	}

	public void search(final Dto searchedViewable) {
		this.searchViewable = searchedViewable;
		doSearchForPage(1);
	}

	private void doSearchForPage(final int page) {
		LoadIndicator.get().startLoading();

		commonService.search(dto.getModule(), searchViewable, getOffsetForPage(page), getOffsetForPage(page) + MAX_ENTRIES, new AsyncCallback<ListQueryResult<Dto>>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(ListQueryResult<Dto> result) {
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
		// this.searchViewable.setMarked(true);
		// search(searchViewable);
	}

	private void doMarkedSearchForPage(final int page) {
		LoadIndicator.get().startLoading();

		commonService.getAllMarked(dto.getModule(), getOffsetForPage(page), getOffsetForPage(page) + MAX_ENTRIES, new AsyncCallback<ListQueryResult<Dto>>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(ListQueryResult<Dto> result) {
				insertSearchResults(page, result);
			}
		});
	}

	private void insertSearchResults(final int page, ListQueryResult<Dto> result) {
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
