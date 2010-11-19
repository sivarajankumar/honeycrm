package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.mvp.events.SuccessfulLoginEvent;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getLoginButton();
		HasValue<String> getLogin();
		HasValue<String> getPassword();
		Widget asWidget();
		HasKeyDownHandlers getLoginAsKeyHandler();
		HasText getStatusLabel();
		void hide();
		void onCannotGetConfiguration();
	}

	private static final boolean AUTO_LOGIN = false;
	private static final String AUTO_LOGIN_USERNAME = "james";
	private static final String AUTO_LOGIN_PASSWORD = "";
	private final AuthServiceAsync authService;
	private final ConfigServiceAsync confService;
	private final SimpleEventBus eventBus;
	private final Display view;
	private boolean loginDeferred = false;
	private boolean initializationDone = false;

	public LoginPresenter(final AuthServiceAsync authService, final ConfigServiceAsync confService, SimpleEventBus eventBus, Display loginView) {
		this.authService = authService;
		this.confService = confService;
		this.eventBus = eventBus;
		this.view = loginView;

		bind();
		getConfiguration();
	}

	private void getConfiguration() {
		// While the user stares at his screen we start getting the Honeycrm configuration from the server.
		// This is done independently of the login.
		confService.getConfiguration(new AsyncCallback<Configuration>() {
			@Override
			public void onSuccess(final Configuration result) {
				DtoModuleRegistry.create(result);

				initializationDone = true;

				if (loginDeferred || AUTO_LOGIN) {
					tryLogin();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				view.onCannotGetConfiguration();
			}
		});
	}

	private void tryLogin() {
		if (initializationDone) {
			// use magic login and password when automatic login is on
			final String login = AUTO_LOGIN ? AUTO_LOGIN_USERNAME : view.getLogin().getValue();
			final String pass = AUTO_LOGIN ? AUTO_LOGIN_PASSWORD : view.getPassword().getValue();

			view.getStatusLabel().setText("Checking credentials..");
			authService.login(login, pass, new AsyncCallback<Long>() {
				@Override
				public void onSuccess(Long result) {
					if (null == result) {
						view.getStatusLabel().setText("Invalid login or password.");
					} else {
						view.getStatusLabel().setText("Login successful.");
						eventBus.fireEvent(new SuccessfulLoginEvent(login, result));
						view.hide();
					}
				}

				@Override
				public void onFailure(Throwable caught) {

				}
			});
		} else {
			view.getStatusLabel().setText("Initializing..");
			loginDeferred = true;
			return;
		}
	}

	private void bind() {
		view.getLoginAsKeyHandler().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					tryLogin();
				}
			}
		});
		view.getLoginButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tryLogin();
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		populate();
	}

	private void populate() {
		view.getLogin().setValue("james");
	}
}
