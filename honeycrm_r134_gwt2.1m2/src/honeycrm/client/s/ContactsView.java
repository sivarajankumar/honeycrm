package honeycrm.client.s;

import honeycrm.client.s.ContactsPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class ContactsView extends LocalizedView implements Display {

	private static ContactsViewUiBinder uiBinder = GWT.create(ContactsViewUiBinder.class);

	interface ContactsViewUiBinder extends UiBinder<Widget, ContactsView> {
	}

	@UiField Button createBtn;
	@UiField Button deleteBtn;
	@UiField Button editBtn;
	
	public ContactsView() {
		initWidget(uiBinder.createAndBindUi(this));
		createBtn.setText(constants.create());
		deleteBtn.setText(constants.delete());
		editBtn.setText(constants.edit());
	}

	@UiFactory SimplePager makePager() {
		return new SimplePager(TextLocation.CENTER);
	}

	@Override
	public HasClickHandlers getCreate() {
		return createBtn;
	}

	@Override
	public void showCreate() {
		
	}
}
