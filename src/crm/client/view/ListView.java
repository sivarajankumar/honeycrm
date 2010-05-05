package crm.client.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.TabCenterView;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

// TODO track which items are already in the cache
// TODO update cached items when forced to do so (everytime user opens a page the first time) AND in the background (polling)
// TODO update item count periodically
public class ListView extends AbstractView {
	/**
	 * Map pages to listviewable arrays.
	 */
	protected Map<Integer, Viewable[]> cache = new HashMap<Integer, Viewable[]>();
	protected static final int MAX_ENTRIES = 10;
	private static final int HEADER_ROWS = 1;
	/**
	 * number of columns in front of the actual data containing columns (e.g. delete checkbox column)
	 */
	private static final int LEADING_COLS = 1;

	private int currentPage = -1;
	private int numberOfPages = -1;

	private final Label label = new Label();
	private final FlowPanel panel = new FlowPanel();
	private final FlexTable table = new FlexTable();
	private final ListViewDeletionPanel deletePanel;

	public ListView(final Class<? extends AbstractDto> clazz) {
		super(clazz);

		initHeader();

		// css settings
		table.setBorderWidth(0);
		table.setCellSpacing(0);
		table.setCellPadding(0);
		table.setWidth("100%");
		table.setStyleName("list_table");
		panel.setStyleName("fullWidth");
		label.setStyleName("list_pagination_label");

		HTMLTable.RowFormatter formatter = table.getRowFormatter();
		formatter.setStyleName(0, "table_header");

		// add css class for every row
		for (int i = 1; i < MAX_ENTRIES; i++) {
			formatter.setStyleName(i, "table_row");
		}

		panel.add(table);
		panel.add(new ListViewPaginationBar(this, label));

		// initialize the leading columns
		table.setWidget(0, 0, deletePanel = new ListViewDeletionPanel(clazz, this));

		refresh();

		initWidget(panel);
	}

	private void initHeader() {
		final int[] fieldIds = viewable.getListViewColumnIds();

		for (int i = 0; i < fieldIds.length; i++) {
			table.setText(0, LEADING_COLS + i, viewable.getFieldById(fieldIds[i]).getLabel());
		}
	}

	protected void setNumberOfPages(final int itemCount) {
		if (itemCount < MAX_ENTRIES) {
			numberOfPages = 1;
		} else {
			numberOfPages = itemCount / MAX_ENTRIES;
			if (itemCount > MAX_ENTRIES) {
				++numberOfPages;
			}
		}
		updatePageCounterLabel();
	}

	private void set(final int row, final Viewable listViewable) {
		final ClickHandler showDetailViewHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TabCenterView.instance().get(clazz).showDetailView(listViewable.getId());
				// CenterView.get().showDetailView(clazz, listViewable.getId());

				// TODO: clear all also colored rows when paginate
				HTMLTable.RowFormatter formatterSet = table.getRowFormatter();
				// clear all colored rows
				for (int i = 1; i < MAX_ENTRIES; i++) {
					formatterSet.setStyleName(i, "table_row");
				}
				// draw row
				formatterSet.setStyleName(row + HEADER_ROWS, "table_row selected");
			}
		};

		final int[] ids = listViewable.getListViewColumnIds();

		// add the leading columns (e.g. delete checkbox) before actual data
		table.setWidget(HEADER_ROWS + row, 0, deletePanel.getDeleteCheckboxFor(listViewable.getId(), currentPage));

		for (int i = 0; i < ids.length; i++) {
			final Widget w = getWidgetByType(listViewable, ids[i], true);

			// TODO mouse pointer should be changed on mouse over or label should look like link.
			if (w instanceof Label) {
				((Label) w).addClickHandler(showDetailViewHandler);
				// table.setWidget(HEADER_ROWS + row, LEADING_COLS + i, w);
			} else if (w instanceof Hyperlink) {
				// TODO call history.newItem to manage transition..
				// ((Hyperlink) w).addClickHandler(showDetailViewHandler);
			}

			table.setWidget(HEADER_ROWS + row, LEADING_COLS + i, w);
		}
	}

	// 1. seite 0 - 9
	// 2. seite 10 - 19
	// (1-1)*20 = 0
	// (2-1)*20 = 20
	// (3-1)*20 = 40
	protected void showPage(final int page) {
		if (1 <= page && page <= numberOfPages) {
			if (cache.containsKey(page)) {
				final Viewable[] values = cache.get(page);

				for (int i = 0; i < values.length; i++) {
					set(i, values[i]);
				}

				removeTrailingRows(values.length);

				currentPage = page;
				updatePageCounterLabel();
			} else {
				refreshPage(page);
			}
		}
	}

	/**
	 * empty the trailingRowsCounter number of rows at the end of the table
	 */
	private void removeTrailingRows(final int numberOfValues) {
		final boolean displayingTooManyRows = table.getRowCount() > numberOfValues + HEADER_ROWS;

		if (displayingTooManyRows) {
			final int trailingRowsCounter = table.getRowCount() - HEADER_ROWS - numberOfValues;

			for (int i = 0; i < trailingRowsCounter; i++) {
				table.removeRow(table.getRowCount() - 1);
			}
		}
	}

	private void refreshPage(final int page) {
		final int offset = (-1 == page) ? (0) : (page - 1) * MAX_ENTRIES;

		assert 0 <= offset;

		LoadIndicator.get().startLoading();

		commonService.getAll(IANA.mashal(clazz), offset, offset + MAX_ENTRIES, new AsyncCallback<ListQueryResult<? extends Viewable>>() {
			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
				Window.alert("Could not get data");
			}

			@Override
			public void onSuccess(ListQueryResult<? extends Viewable> result) {
				LoadIndicator.get().endLoading();
				if (-1 == page) {
					currentPage = 1;
				}
				setNumberOfPages(result.getItemCount());
				cache.put(-1 == page ? 1 : page, result.getResults());
				showPage(-1 == page ? 1 : page);
			}
		});
	}

	public int currentPage() {
		return currentPage;
	}

	/*
	 * Deletion
	 */
	public Set<Long> toggleAllForDeletion(final boolean shouldBeDeleted) {
		final Set<Long> ids = new HashSet<Long>();

		for (int y = HEADER_ROWS; y < table.getRowCount(); y++) {
			assert table.getWidget(y, 0) instanceof CheckBox;

			((CheckBox) table.getWidget(y, 0)).setValue(shouldBeDeleted);

			if (shouldBeDeleted) {
				// determine id of the corresponding list viewable item and add it to the set of IDs that will be deleted.
				ids.add(cache.get(currentPage)[y - HEADER_ROWS].getId());
			}
		}

		return ids;
	}

	/*
	 * Pagination
	 */
	private void updatePageCounterLabel() {
		label.setText("Page " + currentPage + " of " + numberOfPages);
	}

	public void showLastPage() {
		showPage(numberOfPages);
	}

	public void showFirstPage() {
		showPage(1);
	}

	public void showPageLeft() {
		showPage(currentPage - 1);
	}

	public void showPageRight() {
		showPage(currentPage + 1);
	}

	public void refresh() {
		refreshPage(currentPage);
	}

	public void deleteSelected() {
		deletePanel.deleteSelected();
	}
}
