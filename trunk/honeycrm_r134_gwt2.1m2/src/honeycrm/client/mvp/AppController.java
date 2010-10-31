package honeycrm.client.mvp;

import honeycrm.client.dto.Dto;
import honeycrm.client.login.User;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.mvp.events.DeleteEvent;
import honeycrm.client.mvp.events.DeleteEventHandler;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.SuccessfulLoginEvent;
import honeycrm.client.mvp.events.SuccessfulLoginEventHandler;
import honeycrm.client.mvp.presenters.ApplicationPresenter;
import honeycrm.client.mvp.presenters.LoginPresenter;
import honeycrm.client.mvp.views.ApplicationView;
import honeycrm.client.mvp.views.LoginView;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements ValueChangeHandler<String> {
	private final ConfigServiceAsync confService;
	private final ReadServiceAsync readService;
	private final AuthServiceAsync authService;
	private final CreateServiceAsync createService;
	private final UpdateServiceAsync updateService;
	private final DeleteServiceAsync deleteService;
	private final SimpleEventBus eventBus;
	private HasWidgets container;
	private boolean initialized = false;

	public AppController(final ReadServiceAsync readService, final CreateServiceAsync createService, final UpdateServiceAsync updateService, final DeleteServiceAsync deleteService, final AuthServiceAsync authService, final ConfigServiceAsync confService, final SimpleEventBus eventBus) {
		this.authService = authService;
		this.createService = createService;
		this.updateService = updateService;
		this.readService = readService;
		this.deleteService = deleteService;
		this.confService = confService;
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(SuccessfulLoginEvent.TYPE, new SuccessfulLoginEventHandler() {
			@Override
			public void onLogin(final SuccessfulLoginEvent event) {
				startInitialisation(event);
			}
		});
		eventBus.addHandler(DeleteEvent.TYPE, new DeleteEventHandler() {
			@Override
			public void onDeleteEvent(final DeleteEvent event) {
				doDelete(event);
			}
		});
	}

	private void doDelete(final DeleteEvent event) {
		deleteService.deleteAll(event.getKind(), event.getDeleteIds(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(final Void result) {

			}

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert("Could not delete");
			}
		});
	}

	private void startInitialisation(final SuccessfulLoginEvent event) {
		this.initialized  = true;
		User.initUser(event.getUserId(), event.getLogin());
		History.newItem("initialized");
		History.fireCurrentHistoryState();
	}

	@Override
	public void onValueChange(final ValueChangeEvent<String> event) {
		final String token = History.getToken();

		if (null != token) {
			if (token.equals("logout") || !initialized || token.equals("login")) {
				new LoginPresenter(authService, confService, eventBus, new LoginView()).go(container);
			} else if (token.equals("initialized")) {
				GWT.runAsync(AppController.class, new RunAsyncCallback() {
					@Override
					public void onSuccess() {
						new ApplicationPresenter(User.getUserId(), readService, createService, updateService, eventBus, new ApplicationView()).go(container);
					}
					
					@Override
					public void onFailure(final Throwable reason) {
						
					}
				});
			} else if (token.split("\\s+").length == 3) {
				GWT.runAsync(AppController.class, new RunAsyncCallback() {
					@Override
					public void onSuccess() {
						final String[] tokens = token.split("\\s+");

						final String module = tokens[0];
						final ModuleAction action = ModuleAction.fromString(tokens[1]);

						if (null != action) {
							switch (action) {
							case DETAIL:
								final Dto dto = new Dto(module);
								dto.setId(NumberParser.convertToLong(tokens[2]));
								eventBus.fireEvent(new OpenEvent(dto));
							}
						}
					}
					
					@Override
					public void onFailure(final Throwable reason) {
					}
				});
			}
		}
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("login");
		} else {
			History.fireCurrentHistoryState();
		}
	}

}
