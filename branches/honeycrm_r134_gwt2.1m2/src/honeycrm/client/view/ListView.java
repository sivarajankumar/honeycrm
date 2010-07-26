package honeycrm.client.view;

import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.TabCenterView;
import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListViewAdapter;
import com.google.gwt.view.client.SelectionModel.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.SelectionChangeHandler;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO track which items are already in the cache
// TODO update cached items when forced to do so (every time user opens a page the first time) AND in
// the background (polling)
// TODO use google formatters instead of field classes of honeycrm 
public class ListView extends AbstractView {
	protected static final int MAX_ENTRIES = 15;

	private boolean showTitle;
	private Button[] additionalButtons;
	private boolean itemsHaveBeenLoadedOnce = false;
	protected final VerticalPanel panel = new VerticalPanel();

	private CellTable<Dto> ct;
	private SimplePager<Dto> pager;
	private ListViewAdapter<Dto> lva;
	
	protected final Panel buttonBar = new HorizontalPanel();

	public ListView(final String clazz) {
		super(clazz);
		initWidget(panel);
	}
	
	public void setAdditionalButtons(Button ... additionalButtons) {
		this.additionalButtons = additionalButtons;
	}

	private Button getDeleteButton() {
		final Button btn = new Button("Delete");
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm("Do you want to delete the selected items?")) {
					deleteSelected(getDeletedIds());
				}
			}

			private void deleteSelected(final Set<Long> ids) {
				LoadIndicator.get().startLoading();

				ServiceRegistry.commonService().deleteAll(moduleDto.getModule(), ids, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						LoadIndicator.get().endLoading();
						refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						displayError(caught);
					}
				});
			}

			private Set<Long> getDeletedIds() {
				final Set<Long> ids = new HashSet<Long>();

				for (final Dto dto : lva.getList()) {
					if (null != dto.get("deleteFlag") && (Boolean) dto.get("deleteFlag")) {
						ids.add(dto.getId());
					}
				}
				return ids;
			}
		});
		return btn;
	}

	protected void initListView() {
		pager = new SimplePager<Dto>(ct = new CellTable<Dto>(), TextLocation.CENTER);
		lva = new ListViewAdapter<Dto>();

		final SingleSelectionModel<Dto> selectionModel = new SingleSelectionModel<Dto>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeHandler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				final Dto dto = selectionModel.getSelectedObject();
				TabCenterView.instance().showModuleTabWithId(dto.getModule(), dto.getId());
			}
		});

		ct.setSelectionEnabled(true);
		ct.setSelectionModel(selectionModel);
		ct.setPageSize(MAX_ENTRIES);
		lva.addView(ct);

		pager.firstPage();
		panel.add(buttonBar);
		panel.add(ct);
		panel.add(pager);

		initButtonBar();
		initListViewHeaderRow();
	}

	private void initButtonBar() {
		if (showTitle) {
			buttonBar.add(getTitleLabel());
		}
		
		buttonBar.add(getDeleteButton());
		
		if (null != additionalButtons) {
			for (final Button additionalButton: additionalButtons) {
				buttonBar.add(additionalButton);
			}
		}
	}

	private void initListViewHeaderRow() {
		addDeleteColumn();

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String id = moduleDto.getListFieldIds()[col];

			final Column<Dto, String> column = new TextColumn<Dto>() {
				@Override
				public String getValue(Dto object) {
					return String.valueOf(object.get(id));
				}
			};

			// TODO what is that for?
			column.setFieldUpdater(new FieldUpdater<Dto, String>() {
				@Override
				public void update(int index, Dto object, String value) {
					object.set(id, value);
				}
			});

			ct.addColumn(column, moduleDto.getFieldById(id).getLabel());
		}
	}

	private void addDeleteColumn() {
		final Column<Dto, Boolean> delCol = new Column<Dto, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(Dto object) {
				return false;
			}
		};
		delCol.setFieldUpdater(new FieldUpdater<Dto, Boolean>() {
			@Override
			public void update(int index, Dto object, Boolean value) {
				// mark this item for later deletion
				object.set("deleteFlag", value);
			}
		});

		// TODO how can we observe checkbox state changes?
		final Header<Boolean> h = new Header<Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue() {
				LogConsole.log("get value");
				return false;
			}
		};

		ct.addColumn(delCol, h);
	}

	protected void refreshListViewValues(ListQueryResult result) {
		if (!itemsHaveBeenLoadedOnce) {
			initListView();
			itemsHaveBeenLoadedOnce = true;
		}
		
		ArrayList<Dto> values = new ArrayList<Dto>();
		for (final Dto dto : result.getResults()) {
			values.add(dto);
		}

		// give the ListViewAdapter our data
		lva.setList(values);

		ct.setDataSize(result.getItemCount(), true);
	}

	private void refreshPage(final int page) {
		log("refreshPage " + page);

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
				refreshListViewValues(result);
				LoadIndicator.get().endLoading();
			}
		});
	}

	private void log(String string) {
		LogConsole.log("[" + moduleDto.getModule() + "] " + string);
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

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	private Label getTitleLabel() {
		final Label title = new Label(moduleDto.getTitle() + "s");
		// TODO add style for this in custom css file
		title.setStyleName("relationship_title");
		return title;
	}
}
