package honeycrm.client.s;

import honeycrm.client.dto.Dto;
import honeycrm.client.s.ContactsPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

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
	
	public ContactsView() {
		initWidget(uiBinder.createAndBindUi(this));

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
		return d;
	}
}
