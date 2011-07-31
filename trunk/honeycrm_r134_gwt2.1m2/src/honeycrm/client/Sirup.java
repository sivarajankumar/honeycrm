package honeycrm.client;

import honeycrm.client.mvp.events.LocaleChangeEvent;
import honeycrm.client.mvp.events.LocaleChangeEventHandler;
import honeycrm.client.s.AppPresenter;
import honeycrm.client.s.AppView;
import honeycrm.client.s.AuthEvent;
import honeycrm.client.s.AuthEventHandler;
import honeycrm.client.s.AuthPresenter;
import honeycrm.client.s.AuthView;
import honeycrm.client.s.ShortcutEvent;
import honeycrm.client.s.ShortcutEventHandler;
import honeycrm.client.services.AuthService;
import honeycrm.client.services.AuthServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Sirup implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final SimpleEventBus bus = new SimpleEventBus();
		
		AuthPresenter p = new AuthPresenter(bus, new AuthView());
		p.go(RootPanel.get());
		
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
								AppPresenter appPresenter = new AppPresenter(bus, new AppView());
								appPresenter.go(RootPanel.get());
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
						PopupPanel pop = new PopupPanel(true);
						pop.add(new Label("received shortcut"));
						RootPanel.get().add(pop);
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
