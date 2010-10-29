package honeycrm.client.mvp.views;

import honeycrm.client.mvp.presenters.LoginPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements Display {

	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

	@UiTemplate("LoginView.ui.xml")
	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}

	public LoginView() {
		initWidget(uiBinder.createAndBindUi(this));

		table.setWidget(0, 0, new Label("Login"));
		table.setWidget(0, 1, login);
		table.setWidget(1, 0, new Label("Password"));
		table.setWidget(1, 1, password);
		table.setWidget(2, 0, loginBtn);
		table.setWidget(3, 0, status);
		table.getFlexCellFormatter().setColSpan(2, 0, 1);

		dialog.setWidget(table);
		dialog.center();
	}

	@UiField
	FlexTable table;

	@UiField
	DialogBox dialog;

	final Button loginBtn = new Button("Login");
	final TextBox login = new TextBox();
	final TextBox password = new TextBox();
	final Label status = new Label();

	@Override
	public HasClickHandlers getLoginButton() {
		return loginBtn;
	}

	@Override
	public HasKeyDownHandlers getLoginAsKeyHandler() {
		return login;
	}
	
	@Override
	public HasValue<String> getLogin() {
		return login;
	}

	@Override
	public HasValue<String> getPassword() {
		return password;
	}

	@Override
	public void hide() {
		dialog.hide();
	}

	@Override
	public HasText getStatusLabel() {
		return status;
	}
}
