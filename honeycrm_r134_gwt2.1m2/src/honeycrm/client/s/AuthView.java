package honeycrm.client.s;

import honeycrm.client.s.AuthPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AuthView extends LocalizedView implements Display {

	private static AuthViewUiBinder uiBinder = GWT.create(AuthViewUiBinder.class);

	interface AuthViewUiBinder extends UiBinder<Widget, AuthView> {
	}

	public AuthView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	TextBox username;
	@UiField
	TextBox password;
	@UiField
	Button loginBtn;
	@UiField
	ListBox language;
	
	@Override
	public HasClickHandlers getLoginBtn() {
		return loginBtn;
	}

	@Override
	public String getUsername() {
		return username.getText();
	}

	@Override
	public String getPassword() {
		return password.getText();
	}
	
	@Override
	public HasChangeHandlers getLanguage() {
		return language;
	}
	
	@UiFactory ListBox makeLanguage() {
		ListBox b = new ListBox();
		b.insertItem("English", "en", 0);
		b.insertItem("German", "de", 1);
		return b;
	}

	@Override
	public String getCurrentLocale() {
		return language.getValue(language.getSelectedIndex());
	}
	
	@Override
	public HasKeyDownHandlers getUsernameField() {
		return username;
	}

	@Override
	public HasKeyDownHandlers getPasswordField() {
		return password;
	}

	@Override
	public void setCurrentLocale(LocaleInfo currentLocale) {
		if ("de".equals(currentLocale.getLocaleName())) {
			language.setItemSelected(1, true);
		} else {
			language.setItemSelected(0, true);
		}
	}
}
