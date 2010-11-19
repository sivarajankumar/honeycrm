package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.mvp.presenters.LoginPresenter.Display;

public class LoginViewMock implements Display {
	private HasValue<String> loginText = new HasStringValueMock();
	private HasKeyDownHandlers loginKeyDownHandler = new HasKeyDownHandlersMock();
	private HasClickHandlers loginBtn = new HasClickHandlersMock();
	private HasValue<String > passwordString = new HasStringValueMock();
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
	public HasText getStatusLabel() {
		return statusLabel;
	}

	@Override
	public void hide() {
	}

	@Override
	public void onCannotGetConfiguration() {
	}
}
