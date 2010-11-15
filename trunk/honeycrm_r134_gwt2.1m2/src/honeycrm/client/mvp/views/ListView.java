package honeycrm.client.mvp.views;

import java.io.Serializable;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.mvp.presenters.ListPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.view.list.ListViewDB;
import honeycrm.client.view.list.ListViewDataProvider;

public class ListView extends Composite implements Display {
	protected static final int DEFAULT_MAX_ENTRIES = 15;
	private int pageSize = DEFAULT_MAX_ENTRIES;
	private ListViewDB db;
	private boolean allowDelete = true;
	final ModuleDto moduleDto;
	private boolean disclose = false;
	private boolean showTitle = false;
	private Button[] additionalButtons;
	private boolean initialized = false;

	private static ListViewUiBinder uiBinder = GWT.create(ListViewUiBinder.class);

	@UiTemplate("ListView.ui.xml")
	interface ListViewUiBinder extends UiBinder<Widget, ListView> {
	}

	@UiField
	DisclosurePanel panel;
	@UiField
	Button addButton;
	@UiField
	Button deleteButton;
	@UiField
	CellTable<Dto> table;
	@UiField
	SimplePager pager;

	final String module;
	private SelectionHandler handler;
	private final ReadServiceAsync readService;	

	public ListView(final String module, final ReadServiceAsync readService) {
		this.readService = readService;
		this.module = module;
		// TODO this is bad - ui should not know about dto module registry..
		this.moduleDto = DtoModuleRegistry.instance().get(module);
		
		initWidget(uiBinder.createAndBindUi(this));

		addButton.setText("Add");
		deleteButton.setText("Delete");
		// db.refresh(); // <- for testing..
	}
	
	public void setAdditionalButtons(Button... additionalButtons) {
		this.additionalButtons = additionalButtons;
	}
	
	public void setDisclose(boolean disclose) {
		this.disclose = disclose;
	}
	
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	private void initListViewHeaderRow() {
		addDeleteColumn();

		for (int col = 0; col < moduleDto.getListFieldIds().length; col++) {
			final String id = moduleDto.getListFieldIds()[col];

			final Column<Dto, ?> column;

			if (moduleDto.getFieldById(id) instanceof FieldBoolean) {
				// TODO make this read-only
				column = new Column<Dto, Boolean>(new CheckboxCell()) {
					@Override
					public Boolean getValue(final Dto object) {
						return (Boolean) object.get(id);
					}
				};
			} else if (moduleDto.getFieldById(id) instanceof FieldRelate) {
				// TODO since this change Memberships cannot be selected / clicked anymore in list views
				column = new Column<Dto, SafeHtml>(new SafeHtmlCell()) {
					@Override
					public SafeHtml getValue(Dto object) {
						final Serializable value = object.get(id);
						
						final SafeHtmlBuilder b = new SafeHtmlBuilder();
						
						if (null == value || 0 == (Long) value) {
							// No related item
						} else {
							final Serializable resolved = object.get(id + "_resolved");
							if (null == resolved || null == ((Dto) resolved).get("name")) {
								// Related item could not be found.
								b.appendHtmlConstant("fail!");
							} else {
								final Dto resolvedDto = (Dto) resolved;
								b.appendHtmlConstant("<a href='#" + resolvedDto.getModule() + " detail " + resolvedDto.getId() + "'>" + resolvedDto.get("name") + "</a>");
							}
						}
						
						return b.toSafeHtml();
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
					return false;
				}
			};

			table.addColumn(delCol, h);
		}
	}
	
	/**
	 * Finishes initialization of table. This is done on request not as part of the constructor call.
	 */
	private void lazyTableInitialisation() {
		final SingleSelectionModel<Dto> selectionModel = new SingleSelectionModel<Dto>(ListViewDB.KEY_PROVIDER);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (null != handler) {
					handler.onSelect(selectionModel.getSelectedObject());
				}
			}
		});

		table.setSelectionModel(selectionModel);
		table.setPageSize(pageSize);
		
		db = new ListViewDB(getListDataProvider());
		db.addDataDisplay(table);

		pager.firstPage();

		if (!showTitle) {
			panel.setHeader(null);
		}
	}

	@Override
	public boolean shouldDelete() {
		return Window.confirm("Do you really want to delete?");
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@UiFactory SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		return pager;
	}
	
	@UiFactory CellTable<Dto> makeTable() {
		pager.setDisplay(table = new CellTable<Dto>(ListViewDB.KEY_PROVIDER));
		return table;
	}


	@Override
	public void setSelectionHandler(SelectionHandler handler) {
		this.handler = handler;
	}
	
	protected ListViewDataProvider getListDataProvider() {
		return new ListViewDataProvider(module, readService);
	}

	@Override
	public void refresh() {
		initialize();
		db.refresh();
	}

	protected void initialize() {
		if (!initialized) {
			initListViewHeaderRow();
			lazyTableInitialisation();

			initialized = true;
		}
	}
	
	@UiFactory DisclosurePanel makePanel() {
		return new DisclosurePanel(module);
	}

	@Override
	public HasClickHandlers getAddButton() {
		return addButton;
	}
}
