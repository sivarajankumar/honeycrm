package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.s.ContactsPresenter.Display;

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
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
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
	@UiField Label contactsNameValue;
	@UiField Label contactsEmailValue;
	@UiField Label contactsPhoneValue;
	@UiField Label contactsNotesValue;
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
	private Dto currentDto = null;
	
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
		
		list.addColumnSortHandler(new AsyncHandler(list));
		provider.addDataDisplay(list);
		provider.refresh(list, list.getColumnSortList());
		
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
		currentDto = new Dto("Contact");
		for (String field: new String[]{"name", "phone", "email", "notes"}) {
			currentDto.set(field, "");
		}

		empty(name, phone, notes, email);
		toggleVisibility(true, name, email, phone, notes);
		toggleVisibility(false, contactsNameValue, contactsEmailValue, contactsPhoneValue, contactsNotesValue);
		grid.setVisible(true);
	}
	
	@UiHandler("editBtn")
	public void handleClickEdit(ClickEvent e) {
		name.setText(String.valueOf(currentDto.get("name")));
		phone.setText(String.valueOf(currentDto.get("phone")));
		email.setText(String.valueOf(currentDto.get("email")));
		notes.setText(String.valueOf(currentDto.get("notes")));
		
		toggleVisibility(true, name, email, phone, notes);
		toggleVisibility(false, contactsNameValue, contactsEmailValue, contactsPhoneValue, contactsNotesValue);
		grid.setVisible(true);
	}
	
	private void empty(HasText ... textFields) {
		for (HasText f: textFields) {
			f.setText("");
		}
	}
	
	private void toggleVisibility(boolean visible, UIObject ... uiObjects) {
		for (UIObject o: uiObjects) {
			o.setVisible(visible);
		}
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
		d.set("notes", notes.getText());
		if (currentDto.getId() > 0)
			d.setId(currentDto.getId());
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
		currentDto = selectedObject;

		contactsNameValue.setText(String.valueOf(selectedObject.get("name")));
		contactsPhoneValue.setText(String.valueOf(selectedObject.get("phone")));
		contactsEmailValue.setText(String.valueOf(selectedObject.get("email")));
		contactsNotesValue.setText(String.valueOf(selectedObject.get("notes")));
		
		toggleVisibility(false, name, email, phone, notes);
		toggleVisibility(true, contactsNameValue, contactsEmailValue, contactsPhoneValue, contactsNotesValue);
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
