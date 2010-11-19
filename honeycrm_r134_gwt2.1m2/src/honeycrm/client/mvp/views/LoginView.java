package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.presenters.LoginPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements Display {
	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}

	private final LocalizedMessages constants;

	public LoginView(final LocalizedMessages constants) {
		this.constants = constants;

		initWidget(uiBinder.createAndBindUi(this));

		table.setWidget(0, 0, new Label(constants.userName()));
		table.setWidget(0, 1, login = new TextBox());
		table.setWidget(1, 0, new Label(constants.password()));
		table.setWidget(1, 1, password = new PasswordTextBox());
		table.setWidget(2, 0, new Label(constants.language()));
		table.setWidget(2, 1, languageBox = createLanguageBox());
		table.setWidget(3, 0, loginBtn = new Button(constants.login()));
		table.setWidget(4, 0, status = new Label());
		table.getFlexCellFormatter().setColSpan(3, 0, 1);

		dialog.setText(constants.loginMessage("james"));
		dialog.setWidget(table);
		dialog.center();
	}

	@UiField
	FlexTable table;
	@UiField
	DialogBox dialog;

	final Button loginBtn;
	final TextBox login;
	final PasswordTextBox password;
	final Label status;
	final ListBox languageBox;

	private ListBox createLanguageBox() {
		final ListBox box = new ListBox();
		for (final String locale : constants.availableLocales().split(",")) {
			box.addItem(LocaleInfo.getLocaleNativeDisplayName(locale), locale);

			// select this item because it represents the locale that is currently selected
			if (locale.equals(LocaleInfo.getCurrentLocale().getLocaleName())) {
				box.setSelectedIndex(box.getItemCount() - 1);
			}
		}
		return box;
	}

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

	@Override
	public void onCannotGetConfiguration() {
		Window.alert(constants.cannotGetConfiguration());
	}

	@Override
	public HasChangeHandlers getLanguageBox() {
		return languageBox;
	}

	@Override
	public String getCurrentLocale() {
		return languageBox.getValue(languageBox.getSelectedIndex());
	}
}
