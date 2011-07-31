package honeycrm.server.test.small.mocks;

import honeycrm.client.mvp.presenters.LoginPresenter.Display;

import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewMock implements Display {
	private HasValue<String> loginText = new HasStringValueMock();
	private HasKeyDownHandlers loginKeyDownHandler = new HasKeyDownHandlersMock();
	private HasClickHandlers loginBtn = new HasClickHandlersMock();
	private HasValue<String > passwordString = new HasStringValueMock();
	private HasChangeHandlers languageBox = new HasChangeHandlerMock();
	private HasText statusLabel = new HasTextMock();
	
	@Override
	public HasClickHandlers getLoginButton() {
		return loginBtn;
	}

	@Override
	public HasValue<String> getLogin() {
		return loginText;
	}

	@Override
	public HasValue<String> getPassword() {
		return passwordString;
	}

	@Override
	public Widget asWidget() {
		return null;
	}

	@Override
	public HasKeyDownHandlers getLoginAsKeyHandler() {
		return loginKeyDownHandler;
	}

	@Override
	public void hide() {
	}

	@Override
	public void onCannotGetConfiguration() {
	}

	@Override
	public HasChangeHandlers getLanguageBox() {
		return languageBox;
	}

	@Override
	public String getCurrentLocale() {
		return "en";
	}

	@Override
	public void setStatusCheckCredentials() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusInvalidLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusLoginSuccessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusInitializing() {
		// TODO Auto-generated method stub
		
	}
}
