package honeycrm.client.s;

import honeycrm.client.dto.Dto;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

abstract public class ModuleView extends LocalizedView {
	@UiField Button createBtn;
	@UiField Button deleteBtn;
	@UiField Button editBtn;
	@UiField Button saveBtn;
	@UiField TextBox search;
	@UiField Grid grid;
	@UiField CellTable<Dto> list;
	@UiField SimplePager pager;

	protected Dto currentDto = null;
	protected final Module module;
	protected SingleSelectionModel<Dto> selectionModel;
	protected GenericDataProvider provider;
	protected static final ProvidesKey<Dto> keyProvider = new ProvidesKey<Dto>() {
		@Override
		public Object getKey(Dto item) {
			return null == item ? null : item.getId();
		}
	};
	
	public ModuleView(Module module, GenericDataProvider provider) {
		this.module = module;
		this.provider = provider;
		this.selectionModel = new SingleSelectionModel<Dto>(keyProvider);
	}

	public HasClickHandlers getCreate() {
		return createBtn;
	}

	abstract protected String[] getFieldNames();

	protected void empty(HasText ... textFields) {
		for (HasText f: textFields) {
			f.setText("");
		}
	}

	protected void toggleVisibility(boolean visible, UIObject ... uiObjects) {
		for (UIObject o: uiObjects) {
			o.setVisible(visible);
		}
	}

	public HasClickHandlers getSaveBtn() {
		return saveBtn;
	}

	@UiFactory
	SimplePager makePager() {
		return new SimplePager(TextLocation.CENTER);
	}

	@UiFactory
	CellTable<Dto> makeTable() {
		return new CellTable<Dto>(keyProvider);
	}

	public SelectionModel<Dto> getSelectionHandler() {
		return selectionModel;
	}

	public Dto getSelectedObject() {
		return selectionModel.getSelectedObject();
	}

	public HasKeyDownHandlers getSearchBtn() {
		return search;
	}

	public String getSearch() {
		return search.getText();
	}

	public GenericDataProvider getProvider() {
		return provider;
	}

	public HasData<Dto> getList() {
		return list;
	}

	public ColumnSortList getColSortList() {
		return list.getColumnSortList();
	}

	public void refresh() {
		provider.refresh(list, list.getColumnSortList());
		grid.setVisible(false);
	}
}