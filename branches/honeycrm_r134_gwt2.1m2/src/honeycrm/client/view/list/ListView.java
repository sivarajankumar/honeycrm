package honeycrm.client.view.list;

import honeycrm.client.admin.LogConsole;
import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.dto.Dto;
import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.view.AbstractView;
import honeycrm.client.view.ModuleAction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO track which items are already in the cache
// TODO update cached items when forced to do so (every time user opens a page the first time) AND in
// the background (polling)
// TODO use google formatters instead of field classes of honeycrm 
public class ListView extends AbstractView {
	protected static final int DEFAULT_MAX_ENTRIES = 15;
	private int pageSize = DEFAULT_MAX_ENTRIES;
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
	protected final Panel buttonBar = new HorizontalPanel();

	private CellTable<Dto> table;
	private SimplePager pager;
	private ListViewDB db;

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

				ServiceRegistry.deleteService().deleteAll(moduleDto.getModule(), ids, new AsyncCallback<Void>() {
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

				/*
				 * for (final Dto dto : lva.getList()) { if (null != dto.get("deleteFlag") && (Boolean) dto.get("deleteFlag")) { ids.add(dto.getId()); } }
				 */
				return ids;
			}
		});
		return btn;
	}

	protected void initListView() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);

		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table = new CellTable<Dto>());

		final SingleSelectionModel<Dto> selectionModel = new SingleSelectionModel<Dto>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				final Dto dto = selectionModel.getSelectedObject();
				History.newItem(HistoryTokenFactory.get(dto.getModule(), ModuleAction.DETAIL, dto.getId()));
			}
		});

		table.setKeyProvider(ListViewDB.KEY_PROVIDER);
		selectionModel.setKeyProvider(ListViewDB.KEY_PROVIDER);

		table.setSelectionModel(selectionModel);
		table.setPageSize(pageSize);
		db = new ListViewDB(getListDataProvider());
		db.addDataDisplay(table);

		pager.firstPage();

		/**
		 * Insert button bar before the rest of the table if the title should be shown. Display button bar right under the table otherwise.
		 */
		if (showTitle) {
			if (disclose) {
				final DisclosurePanel disclosurePanel = new DisclosurePanel(getListTitle());
				disclosurePanel.setAnimationEnabled(true);
				// disclosurePanel.setHeader(buttonBar);
				disclosurePanel.setOpen(true); // TODO the open/closed status should be persisted and reconstructed per user

				final VerticalPanel vpanel = new VerticalPanel();
				WidgetJuggler.addToContainer(vpanel, buttonBar, pager, table);

				disclosurePanel.add(vpanel);

				panel.add(disclosurePanel);
			} else {
				WidgetJuggler.addToContainer(panel, buttonBar, pager, table);
			}
		} else {
			WidgetJuggler.addToContainer(panel, pager, table, buttonBar);
		}

		initButtonBar();
		initListViewHeaderRow();
	}

	protected ListViewDataProvider getListDataProvider() {
		return new ListViewDataProvider(moduleDto.getModule());
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
			WidgetJuggler.addToContainer(buttonBar, /* getSelectionWidget(), */getDeleteButton());
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

			final Column<Dto, ?> column;

			if (moduleDto.getFieldById(id) instanceof FieldBoolean) {
				// TODO make this read-only
				column = new Column<Dto, Boolean>(new CheckboxCell(true)) {
					@Override
					public Boolean getValue(final Dto object) {
						return (Boolean) object.get(id);
					}
				};
			} else if (moduleDto.getFieldById(id) instanceof FieldRelate) {
				// TODO since this change Memberships cannot be selected / clicked anymore in list views
				column = new TextColumn<Dto>() {
					@Override
					public String getValue(final Dto object) {
						final Serializable value = object.get(id);

						if (null == value || 0 == (Long) value) {
							return "";
						} else {
							final Serializable resolved = object.get(id + "_resolved");
							if (null == resolved || null == ((Dto) resolved).get("name")) {
								return "fail!";
							} else {
								final Dto resolvedDto = (Dto) resolved;
								return "<a href='#" + resolvedDto.getModule() + " detail " + resolvedDto.getId() + "'>" + resolvedDto.get("name") + "</a>";
								// return (String) ((Dto) resolved).get("name");
							}
						}
					}
				};
			} else {
				column = new TextColumn<Dto>() {
					// TODO this is hotspot #4 in the benchmark (firebug, detailed output style)
					@Override
					public String getValue(final Dto object) {
						return moduleDto.getFieldById(id).internalFormattedValue(object.get(id));
					}
				};
			}

			table.addColumn(column, moduleDto.getFieldById(id).getLabel());
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

			table.addColumn(delCol, h);
		}
	}

	public void refresh() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (!itemsHaveBeenLoadedOnce) {
					initListView();
					itemsHaveBeenLoadedOnce = true;
				}
				db.refresh();
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("could not run code asynchronously.");
			}
		});
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
