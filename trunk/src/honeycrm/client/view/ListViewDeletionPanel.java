package honeycrm.client.view;

import honeycrm.client.LoadIndicator;
import honeycrm.client.dto.Dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;


/**
 * Responsible for the selection and later deletion of items depending on the page currently
 * displayed in the listview.
 */
public class ListViewDeletionPanel extends AbstractView {
	private final ListView listview;
	/**
	 * Store the delete lists per list view page Each map entry represents one page storing a set of
	 * ids that should be deleted.
	 */
	private final HashMap<Integer, Set<Long>> deleteIdsPerPage = new HashMap<Integer, Set<Long>>();
	private final CheckBox deleteAllCheckbox = new CheckBox();

	public ListViewDeletionPanel(final Dto clazz, final ListView listview) {
		super(clazz);

		this.listview = listview;

		deleteAllCheckbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				updateDeleteSelection(event.getValue());
			}
		});

		initWidget(deleteAllCheckbox);
	}

	public void deleteSelected() {
		// TODO let the user confirm the deletion again

		final int currentPage = listview.currentPage();

		if (deleteIdsPerPage.containsKey(currentPage)) {
			LoadIndicator.get().startLoading();

			final Set<Long> ids = deleteIdsPerPage.get(currentPage);

			commonService.deleteAll(dto.getModule(), ids, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					LoadIndicator.get().endLoading();
					listview.refresh();
					deleteAllCheckbox.setValue(false); // uncheck checkbox after refresh
				}

				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}
			});
		} else {
			// this page has no items selected for deletion. nothing has to be done.
		}
	}

	protected void updateDeleteSelection(final boolean shouldBeDeleted) {
		final int page = listview.currentPage();
		initializeSet(page);

		assert deleteIdsPerPage.containsKey(page) && deleteIdsPerPage.get(page) != null;

		// remove all ids from the set ..
		deleteIdsPerPage.get(page).clear();

		// and add those again that have been selected now.
		for (final long id : listview.toggleAllForDeletion(shouldBeDeleted)) {
			addToDeleteList(id, page, shouldBeDeleted);
		}
	}

	/**
	 * Returns a new checkbox for a viewable item with the given id.
	 */
	public CheckBox getDeleteCheckboxFor(final long id, final int page) {
		final CheckBox checkbox = new CheckBox();
		checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				addToDeleteList(id, page, event.getValue());
			}
		});

		return checkbox;
	}

	protected void addToDeleteList(final long id, final int page, final boolean deleteChecked) {
		initializeSet(page);

		if (deleteChecked) {
			deleteIdsPerPage.get(page).add(id);
		} else {
			deleteIdsPerPage.get(page).remove(id);
		}
	}

	/**
	 * Prepare the map entry for its first use. Create a new empty set at the desired position if it
	 * does not exist yet.
	 * 
	 * @param page
	 */
	private void initializeSet(final int page) {
		if (!deleteIdsPerPage.containsKey(page)) {
			deleteIdsPerPage.put(page, new HashSet<Long>());
		}
	}

	public void deleteAll() {
		LoadIndicator.get().startLoading();

		commonService.deleteAll(dto.getModule(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(Void result) {
				LoadIndicator.get().endLoading();
				listview.refresh();
			}
		});
	}
}
