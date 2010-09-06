package honeycrm.client.login;

import honeycrm.client.misc.Callback;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.misc.WidgetJuggler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginScreen extends DialogBox {
	private final TextBox loginBox, passwordBox;
	private final Button loginBtn;
	private final Widget errorLbl;
	private static final boolean LOGIN_DISABLED = false;

	public LoginScreen(final Callback callback) {
		final FlexTable table = new FlexTable();
		table.setWidget(0, 0, new Label("Login:"));
		table.setWidget(0, 1, loginBox = getLogin(callback));
		table.setWidget(1, 0, new Label("Password:"));
		table.setWidget(1, 1, passwordBox = new TextBox());
		table.setWidget(2, 0, errorLbl = getErrorLabel());
		table.setWidget(3, 0, loginBtn = getLoginButton(callback));

		table.getFlexCellFormatter().setColSpan(2, 0, 1);

		setGlassEnabled(true);
		// setAnimationEnabled(true);
		setText("Please login to honeycrm. Use login 'james' with no password for testing."); // suggest james test login for testing
		setWidget(table);
		show();
		center();
	}

	private TextBox getLogin(final Callback callback) {
		final TextBox box = new TextBox();
		box.setValue("james"); // suggest a sample login
		box.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					tryLogin(callback);
				}
			}
		});
		return box;
	}

	private Widget getErrorLabel() {
		final Widget error = WidgetJuggler.addStyles(new Label("Login failed"), "error");
		error.setVisible(false);
		return error;
	}

	private Button getLoginButton(final Callback callback) {
		final Button button = new Button("Login");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tryLogin(callback);
			}
		});
		return button;
	}

	private void tryLogin(final Callback callback) {
		errorLbl.setVisible(false);
		
		if (LOGIN_DISABLED) {
			hide();
			callback.callback();
		} else {
			allowLogin(false);
			
			ServiceRegistry.authService().login(loginBox.getText(), "password", new AsyncCallback<Long>() {
				@Override
				public void onSuccess(Long result) {
					allowLogin(true);
					
					if (null == result || 0 == result) {
						errorLbl.setVisible(true);
						allowLogin(true); // allow user to login again with new login/password
					} else {
						errorLbl.setVisible(false);
						User.initUser(result, loginBox.getText());
						// hide();
						hide();
						setVisible(false);
						callback.callback();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLbl.setVisible(true);
					allowLogin(true); // allow user to login again with new login/password
				}
			});
		}
	}

	private void allowLogin(boolean allow) {
		loginBox.setEnabled(allow);
		passwordBox.setEnabled(allow);
		loginBtn.setEnabled(allow);
	}
}
