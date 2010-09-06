package honeycrm.client.view;

import honeycrm.client.admin.LogConsole;
import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.misc.WidgetJuggler;

import java.io.Serializable;
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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListViewAdapter;
import com.google.gwt.view.client.PagingListView;
import com.google.gwt.view.client.SelectionModel.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.SelectionChangeHandler;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO track which items are already in the cache
// TODO update cached items when forced to do so (every time user opens a page the first time) AND in
// the background (polling)
// TODO use google formatters instead of field classes of honeycrm 
public class ListView extends AbstractView {
	protected static final int DEFAULT_MAX_ENTRIES = 15;

	private int pageSize = DEFAULT_MAX_ENTRIES;
	private int currentPage = -1;
	private boolean showTitle;
	/**
	 * true if users should have the possibility to hide away the list view. false otherwise.
	 */
	private boolean disclose;
	/**
	 * true if users should have the possibility to delete items i.e. the delete ui components should be displayed. false otherwise.
	 */
	private boolean allowDelete = true;
	private Button[] additionalButtons;
	private boolean itemsHaveBeenLoadedOnce = false;
	protected final VerticalPanel panel = new VerticalPanel();

	private CellTable<Dto> ct;
	private SimplePager<Dto> pager;
	private ListViewAdapter<Dto> lva;

	protected final Panel buttonBar = new HorizontalPanel();

	public ListView(final String module) {
		super(module);
		initWidget(panel);
	}

	private Button getDeleteButton() {
		final Button btn = new Button("Delete selected");
		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getDeletedIds().size() > 0) {
					if (Window.confirm("Do you want to delete the selected items?")) {
						deleteSelected(getDeletedIds());
					}
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
						LoadIndicator.get().endLoading();
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
		pager = new SimplePager<Dto>(ct = new CellTable<Dto>(), TextLocation.CENTER) {
			@Override
			public void onRangeOrSizeChanged(final PagingListView<Dto> listView) {
				super.onRangeOrSizeChanged(listView);

				/**
				 * only do something if items have already been loaded
				 */
				if (itemsHaveBeenLoadedOnce) {
					final int newPage = 1 + listView.getPageStart() / listView.getPageSize();
					final boolean changedPage = newPage != currentPage;

					if (changedPage) {
						/**
						 * retrieve items of the selected page
						 */
						refreshPage(newPage);
					}
				}
			};
		};

		lva = new ListViewAdapter<Dto>();

		final SingleSelectionModel<Dto> selectionModel = new SingleSelectionModel<Dto>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeHandler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				final Dto dto = selectionModel.getSelectedObject();
				History.newItem(HistoryTokenFactory.get(dto.getModule(), ModuleAction.DETAIL, dto.getId()));
			}
		});

		ct.setSelectionEnabled(true);
		ct.setSelectionModel(selectionModel);
		ct.setPageSize(pageSize);
		lva.addView(ct);

		pager.firstPage();

		/**
		 * Insert button bar before the rest of the table if the title should be shown. Display button bar right under the table otherwise.
		 */
		if (showTitle) {
			if (disclose) {
				final DisclosurePanel disclosurePanel = new DisclosurePanel(getListTitle());
				disclosurePanel.setAnimationEnabled(true);
			//	disclosurePanel.setHeader(buttonBar);
				disclosurePanel.setOpen(true); // TODO the open/closed status should be persisted and reconstructed per user

				final VerticalPanel vpanel = new VerticalPanel();
				WidgetJuggler.addToContainer(vpanel, buttonBar, ct, pager);

				disclosurePanel.add(vpanel);

				panel.add(disclosurePanel);
			} else {
				WidgetJuggler.addToContainer(panel, buttonBar, ct, pager);
			}
		} else {
			WidgetJuggler.addToContainer(panel, ct, pager, buttonBar);
		}

		initButtonBar();
		initListViewHeaderRow();
	}

	/**
	 * Returns the title of the list view widget. Subclasses may override this to display other titles.
	 */
	protected String getListTitle() {
		return moduleDto.getTitle() + "s";
	}

	private void initButtonBar() {
		if (showTitle && !disclose) {
			buttonBar.add(getTitleLabel());
		}

		if (allowDelete) { // only should selection widget and delete button if user is allowed to delete anything
			WidgetJuggler.addToContainer(buttonBar, /*getSelectionWidget(),*/ getDeleteButton());
		}
		
		if (null != additionalButtons) {
			WidgetJuggler.addToContainer(buttonBar, additionalButtons);
		}
	}

	private Widget getSelectionWidget() {
		final ListBox box = new ListBox();
		box.addItem("All items");
		box.addItem("This page");
		
		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label("Select: "));
		panel.add(box);
		return panel;
	}

	private void initListViewHeaderRow() {
		addDeleteColumn();

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String id = moduleDto.getListFieldIds()[col];

			final Column<Dto, String> column = new TextColumn<Dto>() {
				// TODO this is hotspot #4 in the benchmark (firebug, detailed output style)
				@Override
				public String getValue(final Dto object) {
					final Serializable value = object.get(id);

					if (moduleDto.getFieldById(id) instanceof FieldRelate) {
						if (null == value || 0 == (Long) value) {
							return "";
						} else {
							final Serializable resolved = object.get(id + "_resolved");
							if (null == resolved || null == ((Dto) resolved).get("name")) {
								return "fail!";
							} else {
								return (String) ((Dto) resolved).get("name");
							}
						}
					} else {
						return moduleDto.getFieldById(id).internalFormattedValue(value);
					}
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
		if (allowDelete) { // only attach delete column of the user is allowed to delete
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
	}

	// TODO fix pagination implementation
	protected void refreshListViewValues(ListQueryResult result) {
		if (!itemsHaveBeenLoadedOnce) {
			initListView();
			itemsHaveBeenLoadedOnce = true;
		}

		ArrayList<Dto> values = new ArrayList<Dto>();
		for (final Dto dto : result.getResults()) {
			values.add(dto);
		}

		ct.setDataSize(result.getItemCount(), true);
		/*
		 * final int start = (currentPage - 1) * pageSize;
		 * 
		 * ct.setPageStart(start); ct.setData(start, result.getItemCount(), values);
		 */
		// give the ListViewAdapter our data
		lva.setList(values);
		lva.refresh();

		/*
		 * if (null == lva.getList() || 0 == lva.getList().size()) { ct.setData(0, values.size(), values); } else { ct.setData(1 * pageSize, values.size(), values); /* if (lva.getList().size() == values.size()) { // do not call set list again. overwrite list items instead. for (int i = 0; i < values.size(); i++) { lva.getList().set(0, values.get(i)); } } else { Window.alert("Fail! unequal list sizes."); } }
		 */

		// lva.setList(values);
		// lva.refresh();

		// ct.setPageStart((currentPage - 1) * MAX_ENTRIES + 1);
		// if (0 == ct.getDataSize()) {
		// }
	}

	protected void refreshPage(final int page) {
		log("refreshPage " + page);

		final int offset = getOffsetForPage(page);

		assert 0 <= offset;

		LoadIndicator.get().startLoading();
		log("started loading");

		commonService.getAll(moduleDto.getModule(), offset, offset + pageSize, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onFailure(Throwable caught) {
				log("error");
				displayError(caught);
				LoadIndicator.get().endLoading();
			}

			@Override
			public void onSuccess(ListQueryResult result) {
				log("received result");
				currentPage = page;
				refreshListViewValues(result);
				LoadIndicator.get().endLoading();
			}
		});
	}

	private void log(String string) {
		LogConsole.log("[" + moduleDto.getModule() + "] " + string);
	}

	protected int getOffsetForPage(final int page) {
		return (-1 == page) ? (0) : (page - 1) * pageSize;
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

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void setDisclose(boolean disclose) {
		this.disclose = disclose;
	}
	
	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public void setAdditionalButtons(Button... additionalButtons) {
		this.additionalButtons = additionalButtons;
	}

	private Label getTitleLabel() {
		final Label title = new Label(getListTitle());
		// TODO add style for this in custom css file
		title.setStyleName("relationship_title");
		return title;
	}
}
