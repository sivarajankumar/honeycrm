package honeycrm.client;

import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.LocaleChangeEvent;
import honeycrm.client.mvp.events.LocaleChangeEventHandler;
import honeycrm.client.mvp.events.OpenModuleEvent;
import honeycrm.client.s.AppPresenter;
import honeycrm.client.s.AppView;
import honeycrm.client.s.AuthEvent;
import honeycrm.client.s.AuthEventHandler;
import honeycrm.client.s.AuthPresenter;
import honeycrm.client.s.AuthView;
import honeycrm.client.s.ContactsPresenter;
import honeycrm.client.s.ContactsView;
import honeycrm.client.s.ShortcutEvent;
import honeycrm.client.s.ShortcutEventHandler;
import honeycrm.client.services.AuthService;
import honeycrm.client.services.AuthServiceAsync;

import com.google.gwt.core.client.EntryPoint;
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
import com.google.gwt.user.client.ui.RootPanel;

public class Sirup implements EntryPoint {
	private final SimpleEventBus bus = new SimpleEventBus();
	
	@Override
	public void onModuleLoad() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if ("login".equals(event.getValue())) {
					AuthPresenter p = new AuthPresenter(bus, new AuthView());
					p.go(RootPanel.get());
				} else if ("app".equals(event.getValue())) {
					ContactsView contactsView = new ContactsView();
					new ContactsPresenter(bus, contactsView);
					AppPresenter appPresenter = new AppPresenter(bus, new AppView(contactsView));
					appPresenter.go(RootPanel.get());
				}
			}
		});

		History.newItem("login");
		History.fireCurrentHistoryState();
		
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				final AuthServiceAsync authService = GWT.create(AuthService.class);

				bus.addHandler(AuthEvent.TYPE, new AuthEventHandler() {
					@Override
					public void onAuth(AuthEvent event) {
						authService.login(event.getUsername(), event.getPassword(), new AsyncCallback<Long>() {
							@Override
							public void onSuccess(Long result) {
								History.newItem("app");
							}

							@Override
							public void onFailure(Throwable caught) {
							}
						});
					}
				});
				bus.addHandler(ShortcutEvent.TYPE, new ShortcutEventHandler() {
					@Override
					public void onShortcut(ShortcutEvent event) {
						switch (event.getCode()) {
						case 'd': // dashboard
							bus.fireEvent(new OpenModuleEvent("Dashboard"));
							break;
						case 'c': // contacts 
							bus.fireEvent(new OpenModuleEvent("Contact"));
							break;
						case 'n': // new == create
							bus.fireEvent(new OpenModuleEvent("Contact"));
							bus.fireEvent(new CreateEvent("Contact"));
							break;
						}
					}
				});
				bus.addHandler(LocaleChangeEvent.TYPE, new LocaleChangeEventHandler() {
					@Override
					public void onLocaleChangeEvent(LocaleChangeEvent event) {
						UrlBuilder builder = Location.createUrlBuilder().setParameter("locale", event.getLocale());
						Window.Location.replace(builder.buildString());
					}
				});
			}

			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}
}
