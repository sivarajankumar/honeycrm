package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Callback;
import honeycrm.client.s.ContactsPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class ContactsView extends LocalizedView implements Display {

	private static ContactsViewUiBinder uiBinder = GWT.create(ContactsViewUiBinder.class);

	interface ContactsViewUiBinder extends UiBinder<Widget, ContactsView> {
	}

	@UiField Label contactsName;
	@UiField Label contactsEmail;
	@UiField Label contactsPhone;
	@UiField Label contactsNotes;
	@UiField Button createBtn;
	@UiField Button deleteBtn;
	@UiField Button editBtn;
	@UiField Button saveBtn;
	@UiField TextBox name;
	@UiField TextBox email;
	@UiField TextBox phone;
	@UiField TextArea notes;
	@UiField Grid grid;
	@UiField TextBox search;
	
	@UiField CellTable<Dto> list;
	@UiField SimplePager pager;
	
	private SingleSelectionModel<Dto> selectionModel;
	private final ContactsDataProvider provider;
	private final ProvidesKey<Dto> keyProvider = new ProvidesKey<Dto>() {
		@Override
		public Object getKey(Dto item) {
			return null == item ? null : item.getId();
		}
	};
	private long currentId = 0;
	
	public ContactsView(ContactsDataProvider provider) {
		initWidget(uiBinder.createAndBindUi(this));
		pager.setDisplay(list);
		
		this.provider = provider;
		this.selectionModel = new SingleSelectionModel<Dto>(keyProvider);
		list.setSelectionModel(selectionModel);
		list.setPageSize(20);
		pager.firstPage();
		
		final TextColumn<Dto> nameCol = new TextColumn<Dto>() {
			@Override
			public String getValue(Dto object) {
				return String.valueOf(object.get("name"));
			}
		};
		TextColumn<Dto> emailCol = new TextColumn<Dto>() {
			@Override
			public String getValue(Dto object) {
				return String.valueOf(object.get("email"));
			}
		};
		TextColumn<Dto> phoneCol = new TextColumn<Dto>() {
			@Override
			public String getValue(Dto object) {
				return String.valueOf(object.get("phone"));
			}
		};
		nameCol.setSortable(true);
		
		list.addColumn(nameCol, constants.contactsName());
		list.addColumn(emailCol, constants.contactsEmail());
		list.addColumn(phoneCol, constants.contactsPhone());
		
		AsyncProvider.getReadService(new Callback<ReadServiceAsync>() {
			@Override
			public void callback(ReadServiceAsync arg) {
				ContactsView.this.provider.addDataDisplay(list);
				list.addColumnSortHandler(new AsyncHandler(list));
				ContactsView.this.provider.refresh(list, list.getColumnSortList());
			}
		});
		
		createBtn.setText(constants.create());
		deleteBtn.setText(constants.delete());
		editBtn.setText(constants.edit());
		saveBtn.setText(constants.save());
		
		contactsName.setText(constants.contactsName());
		contactsEmail.setText(constants.contactsEmail());
		contactsPhone.setText(constants.contactsPhone());
		contactsNotes.setText(constants.contactsNotes());
	}

	@UiFactory SimplePager makePager() {
		return new SimplePager(TextLocation.CENTER);
	}

	@Override
	public HasClickHandlers getCreate() {
		return createBtn;
	}
	
	@UiHandler("createBtn")
	public void handleClick(ClickEvent e) {
		grid.setVisible(true);
		currentId = 0;
		name.setText("");
		phone.setText("");
		notes.setText("");
		email.setText("");
	}
	
	@Override
	public HasClickHandlers getSaveBtn() {
		return saveBtn;
	}

	@Override
	public Dto getContact() {
		Dto d = new Dto("Contact");
		d.set("name", name.getText());
		d.set("email", email.getText());
		d.set("phone", phone.getText());
		d.set("notes", name.getText());
		if (currentId > 0)
			d.setId(currentId);
		return d;
	}
	
	@UiFactory CellTable<Dto> makeTable() {
		return new CellTable<Dto>(keyProvider);
	}

	@Override
	public SelectionModel<Dto> getSelectionHandler() {
		return selectionModel;
	}
	
	@Override
	public Dto getSelectedObject() {
		return selectionModel.getSelectedObject();
	}

	@Override
	public void openView(Dto selectedObject) {
		currentId  = selectedObject.getId();
		name.setText(String.valueOf(selectedObject.get("name")));
		phone.setText(String.valueOf(selectedObject.get("phone")));
		email.setText(String.valueOf(selectedObject.get("email")));
		notes.setText(String.valueOf(selectedObject.get("notes")));
		grid.setVisible(true);
	}
	
	@Override
	public HasKeyPressHandlers[] getAllFields() {
		return new HasKeyPressHandlers[] {name, phone, email, notes};
	}

	@Override
	public void refresh() {
		provider.refresh(list, list.getColumnSortList());
		grid.setVisible(false);
	}
	
	@Override
	public HasKeyDownHandlers getSearchBtn() {
		return search;
	}
	
	@Override
	public String getSearch() {
		return search.getText();
	}

	@Override
	public ContactsDataProvider getProvider() {
		return provider;
	}

	@Override
	public HasData<Dto> getList() {
		return list;
	}
	
	@Override
	public ColumnSortList getColSortList() {
		return list.getColumnSortList();
	}
}
