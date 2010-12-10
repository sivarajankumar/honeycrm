package honeycrm.client.mvp;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.NumberParser;
import honeycrm.client.misc.User;
import honeycrm.client.mvp.events.DeleteEvent;
import honeycrm.client.mvp.events.DeleteEventHandler;
import honeycrm.client.mvp.events.LocaleChangeEvent;
import honeycrm.client.mvp.events.LocaleChangeEventHandler;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenReportEvent;
import honeycrm.client.mvp.events.SuccessfulLoginEvent;
import honeycrm.client.mvp.events.SuccessfulLoginEventHandler;
import honeycrm.client.mvp.presenters.ApplicationPresenter;
import honeycrm.client.mvp.presenters.CsvImportPresenter;
import honeycrm.client.mvp.presenters.LoginPresenter;
import honeycrm.client.mvp.presenters.PluginPresenter;
import honeycrm.client.mvp.presenters.ReportsSuggestionPresenter;
import honeycrm.client.mvp.views.ApplicationView;
import honeycrm.client.mvp.views.CsvImportView;
import honeycrm.client.mvp.views.LoginView;
import honeycrm.client.mvp.views.PluginView;
import honeycrm.client.mvp.views.ReportsSuggestionView;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements ValueChangeHandler<String> {
	private final ConfigServiceAsync confService;
	private final ReadServiceAsync readService;
	private final AuthServiceAsync authService;
	private final CreateServiceAsync createService;
	private final UpdateServiceAsync updateService;
	private final DeleteServiceAsync deleteService;
	private final PluginServiceAsync pluginService;
	private final ReportServiceAsync reportService;
	private final SimpleEventBus eventBus;
	private HasWidgets container;
	private boolean initialized = false;
	private LocalizedMessages constants;

	public AppController(final LocalizedMessages constants, final ReadServiceAsync readService, final CreateServiceAsync createService, final UpdateServiceAsync updateService, final DeleteServiceAsync deleteService, final AuthServiceAsync authService, final ConfigServiceAsync confService, final PluginServiceAsync pluginService, final ReportServiceAsync reportService, final SimpleEventBus eventBus) {
		this.authService = authService;
		this.createService = createService;
		this.updateService = updateService;
		this.readService = readService;
		this.deleteService = deleteService;
		this.pluginService = pluginService;
		this.confService = confService;
		this.reportService = reportService;
		this.eventBus = eventBus;
		this.constants = constants;
		bind();
	}

	private void bind() {
		if (GWT.isClient()) {
			History.addValueChangeHandler(this);
		}
		
		eventBus.addHandler(SuccessfulLoginEvent.TYPE, new SuccessfulLoginEventHandler() {
			@Override
			public void onLogin(final SuccessfulLoginEvent event) {
				startInitialisation(event);
			}
		});
		eventBus.addHandler(DeleteEvent.TYPE, new DeleteEventHandler() {
			@Override
			public void onDeleteEvent(DeleteEvent event) {
				doDelete(event);
			}
		});
		eventBus.addHandler(LocaleChangeEvent.TYPE, new LocaleChangeEventHandler() {
			@Override
			public void onLocaleChangeEvent(LocaleChangeEvent event) {
				updateLocale(event.getLocale());
			}
		});
	}

	private void doDelete(DeleteEvent event) {
		deleteService.deleteAll(event.getKind(), event.getDeleteIds(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {

			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not delete");
			}
		});
	}

	private void startInitialisation(final SuccessfulLoginEvent event) {
		this.initialized = true;
		User.initUser(event.getUserId(), event.getLogin());
		handleInitialized();
		//History.fireCurrentHistoryState();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String token = History.getToken();

		if (null != token) {
			if (token.equals("logout") || !initialized || token.equals("login")) {
				new LoginPresenter(authService, confService, eventBus, new LoginView(constants)).go(container);
			} else if ("misc".equals(token)) {
				handleMisc();
			} else if ("plugins".equals(token)) {
				handlePlugins();
			} else if ("report".equals(token)) {
				handleReports();
			} else if (token.split("\\s+").length == 2 && "report".equals(token.split("\\s+")[0])) {
				eventBus.fireEvent(new OpenReportEvent(NumberParser.convertToInteger(token.split("\\s+")[1])));
			} else if (token.split("\\s+").length == 2) {
				handleImport(token);
			} else if (token.split("\\s+").length == 3) {
				handleOpen(token);
			}
		}
	}

	private void handlePlugins() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				new PluginPresenter(new PluginView(), readService, pluginService).go(container);
			}
			
			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	private void handleMisc() {
	}

	private void handleReports() {
		GWT.runAsync(AppController.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				new ReportsSuggestionPresenter(new ReportsSuggestionView(), eventBus, reportService).go(container);
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	protected void handleInitialized() {
		GWT.runAsync(AppController.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				new ApplicationPresenter(User.getUserId(), readService, createService, updateService, pluginService, eventBus, new ApplicationView(readService, reportService)).go(container);
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	protected void handleImport(final String token) {
		GWT.runAsync(AppController.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				final String[] tokens = token.split("\\s+");
				if ("import".equals(tokens[1])) {
					new CsvImportPresenter(createService, eventBus, new CsvImportView(), tokens[0]).go(container);
				}
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	protected void handleOpen(final String token) {
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
			public void onFailure(Throwable reason) {
			}
		});
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("login");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	private void updateLocale(final String locale) {
		UrlBuilder builder = Location.createUrlBuilder().setParameter("locale", locale);
		Window.Location.replace(builder.buildString());
	}
}
