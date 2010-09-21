package honeycrm.client.login;

import honeycrm.client.basiclayout.Initializer;
import honeycrm.client.basiclayout.TabCenterView;
import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.misc.WidgetJuggler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.ColumnChart;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class LoginScreen extends DialogBox {
	private TextBox loginBox, passwordBox;
	private Button loginBtn;
	private Label errorLbl;
	private static final boolean LOGIN_DISABLED = false;
	private double loadSteps = 0;
	private boolean loginDeferred = false;

	public LoginScreen(final Callback callback) {
		final FlexTable table = new FlexTable();
		setWidget(table);
		table.setWidget(0, 0, new Label("Login:"));
		table.setWidget(0, 1, loginBox = getLogin(callback));
		table.setWidget(1, 0, new Label("Password:"));
		table.setWidget(1, 1, passwordBox = new TextBox());
		table.setWidget(2, 0, errorLbl = getErrorLabel());
		table.setWidget(3, 0, loginBtn = getLoginButton(callback));
		table.getFlexCellFormatter().setColSpan(2, 0, 1);
		setText("Please login to honeycrm. Use login 'james' with no password for testing."); // suggest james test login for testing
		setGlassEnabled(true);
		setAnimationEnabled(false);
		// setAnimationEnabled(true);
		show();
		center();

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				// only works online.. cannot test without internet
				if (Initializer.SKIP_LOADING_VISUALISATIONS) {
					loadSteps++;
				} else {
					VisualizationUtils.loadVisualizationApi(new Runnable() {
						@Override
						public void run() {
							loadSteps++;

							if (loginDeferred && finishedInitialisation()) {
								tryLogin(callback);
							}
						}
					}, Table.PACKAGE, LineChart.PACKAGE, ColumnChart.PACKAGE);
				}

				ServiceRegistry.configService().getConfiguration(new AsyncCallback<Configuration>() {
					@Override
					public void onSuccess(final Configuration result) {
						DtoModuleRegistry.create(result);

						loadSteps++;

						if (loginDeferred && finishedInitialisation()) {
							tryLogin(callback);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Could not get configuration");
					}
				});
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not execute initialisation code asynchronous.");
			}
		});
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

	private Label getErrorLabel() {
		final Label error = (Label) WidgetJuggler.addStyles(new Label("Login failed"), "error");
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
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (finishedInitialisation()) {
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
									TabCenterView.instance(); // instantiate ui before switching perspective
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
				} else {
					loginDeferred = true;
				}
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not execute code asynchronously.");
			}
		});
	}

	private void allowLogin(boolean allow) {
		loginBox.setEnabled(allow);
		passwordBox.setEnabled(allow);
		loginBtn.setEnabled(allow);
	}

	private boolean finishedInitialisation() {
		return 2 == loadSteps;
	}
}
