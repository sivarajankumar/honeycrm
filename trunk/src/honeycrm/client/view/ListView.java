package honeycrm.client.view;

import honeycrm.client.LoadIndicator;
import honeycrm.client.LoadingPanel;
import honeycrm.client.TabCenterView;
import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

// TODO track which items are already in the cache
// TODO update cached items when forced to do so (every time user opens a page the first time) AND in
// the background (polling)
// TODO implement pagination, deletion
// TODO use google formatters instead of field classes of honeycrm 
public class ListView extends AbstractView {
	protected static final int MAX_ENTRIES = 10;

	private boolean itemsHaveBeenLoadedOnce = false;
	private final Table t;
	private Map<Integer, Dto> rowNrToDto = new HashMap<Integer, Dto>();

	public ListView(final String clazz) {
		super(clazz);

		final VerticalPanel panel = new VerticalPanel();

		if (LoadingPanel.SKIP_LOADING_VISUALISATIONS) {
			t = null;
		} else {
			panel.add(t = new Table());
			t.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					if (t.getSelections().get(0).isRow()) {
						final int rowNr = t.getSelections().get(0).getRow();
						if (rowNrToDto.containsKey(rowNr)) {
							final Dto dto = rowNrToDto.get(rowNr);
							TabCenterView.instance().get(moduleDto.getModule()).showDetailView(dto.getId());
						} else {
							Window.alert("Cannot get dto of row #" + rowNr);
						}
					}
				}
			});
		}

		// do not refresh within the constructor -> this generates too much load during login
		// refresh();

		initWidget(panel);
	}

	private void refreshPage(final int page) {
		log("refreshPage " + page);

		itemsHaveBeenLoadedOnce = true;
		final int offset = getOffsetForPage(page);

		assert 0 <= offset;

		LoadIndicator.get().startLoading();
		log("started loading");

		commonService.getAll(moduleDto.getModule(), offset, offset + MAX_ENTRIES, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onFailure(Throwable caught) {
				log("error");
				displayError(caught);
			}

			@Override
			public void onSuccess(ListQueryResult result) {
				log("received result");
				if (!LoadingPanel.SKIP_LOADING_VISUALISATIONS) {
					t.draw(getTableData(result), getOptions());
				}
				LoadIndicator.get().endLoading();
			}
		});
	}

	private void log(String string) {
		LogConsole.log("[" + moduleDto.getModule() + "] " + string);
	}

	protected Options getOptions() {
		final Options options = Options.create();
		options.setWidth("440px");
		options.setAlternatingRowStyle(true);
		options.setPageSize(MAX_ENTRIES);
		options.setAllowHtml(true);
		// options.set("pagingSymbols", "{prev:'prev',next:'next'}");
		return options;
	}

	protected AbstractDataTable getTableData(ListQueryResult result) {
		log("getTableData " + result.getResults().length + " results");

		final DataTable data = DataTable.create();
		data.addRows(result.getResults().length);

		log("generating header");
		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String id = moduleDto.getListFieldIds()[col];

			data.addColumn(ColumnType.STRING);
			data.setColumnLabel(col, moduleDto.getFieldById(id).getLabel());

			log("set header col #" + col);
		}

		final int moduleColumnCount = moduleDto.getListFieldIds().length;

		data.addColumn(ColumnType.NUMBER);
		data.setColumnLabel(0 + moduleColumnCount, "Id");

		data.addColumn(ColumnType.STRING);
		data.setColumnLabel(1 + moduleColumnCount, getDeleteAllCheckBox().getHTML());

		// final CheckBox deleteAllBox = getDeleteAllCheckBox();

		log("set header colum for id");

		log("inserting real data");
		for (int row = 0; row < result.getResults().length; row++) {
			log("inserting row #" + row);
			final Dto dto = result.getResults()[row];

			rowNrToDto.put(row, dto);

			for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
				log("inserting (row,col)=(" + row + "," + col + ")");

				final String id = moduleDto.getListFieldIds()[col];
				log("#1 -> got id " + id);

				final Widget w = dto.getFieldById(id).getWidget(View.DETAIL, dto.get(id));
				log("#2 -> got widget " + w.getClass());

				try {
					data.setValue(row, col, String.valueOf(dto.getFieldById(id).getData(w)));
					log("#3 -> value is set");
				} catch (RuntimeException e) {
					log("an exception occured during getting data from widget and inserting into table. " + e.getMessage());
				}
			}

			data.setValue(row, 0 + moduleColumnCount, dto.getId());
			data.setValue(row, 1 + moduleColumnCount, "<input type='checkbox' name='asdasdsad' />");
		}

		return data;
	}

	private CheckBox getDeleteAllCheckBox() {
		final CheckBox box = new CheckBox();

		box.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("(un-) check all");
			}
		});

		return box;
	}

	protected int getOffsetForPage(final int page) {
		return (-1 == page) ? (0) : (page - 1) * MAX_ENTRIES;
	}

	public void refresh() {
		refreshPage(1); // TODO refresh all items (was: refresh(currentPage);)
	}

	public void deleteSelected() {
		// deletePanel.deleteSelected();
	}

	public void deleteAll() {
		// deletePanel.deleteAll();
	}

	public boolean isInitialized() {
		return itemsHaveBeenLoadedOnce;
	}
}
