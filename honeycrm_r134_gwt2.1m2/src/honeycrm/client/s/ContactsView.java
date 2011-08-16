package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.s.ContactsPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactsView extends ModuleView implements Display {

	private static ContactsViewUiBinder uiBinder = GWT.create(ContactsViewUiBinder.class);

	interface ContactsViewUiBinder extends UiBinder<Widget, ContactsView> {
	}

	@UiField
	Label contactsName;
	@UiField
	Label contactsEmail;
	@UiField
	Label contactsPhone;
	@UiField
	Label contactsNotes;
	@UiField
	Label contactsNameValue;
	@UiField
	Label contactsEmailValue;
	@UiField
	Label contactsPhoneValue;
	@UiField
	Label contactsNotesValue;
	@UiField
	TextBox name;
	@UiField
	TextBox email;
	@UiField
	TextBox phone;
	@UiField
	TextArea notes;

	public ContactsView(GenericDataProvider provider) {
		super(Module.Contact, provider);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasKeyPressHandlers[] getAllFields() {
		return new HasKeyPressHandlers[] { name, phone, email, notes };
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

	@Override
	public void init(Void arg) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				pager.setDisplay(list);

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

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	@Override
	protected String[] getFieldNames() {
		return new String[] { "name", "email", "phone", "notes" };
	}

	@Override
	protected Label[] getDetailViewFields() {
		return new Label[] { contactsNameValue, contactsEmailValue, contactsPhoneValue, contactsNotesValue };
	}

	@Override
	protected UIObject[] getEditViewFields() {
		return new UIObject[] { name, email, phone, notes };
	}
}
