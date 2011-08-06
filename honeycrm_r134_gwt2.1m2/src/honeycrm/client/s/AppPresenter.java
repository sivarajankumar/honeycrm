package honeycrm.client.s;

import honeycrm.client.mvp.events.OpenModuleEvent;
import honeycrm.client.mvp.events.OpenModuleEventHandler;
import honeycrm.client.mvp.events.RpcBeginEvent;
import honeycrm.client.mvp.events.RpcBeginEventHandler;
import honeycrm.client.mvp.events.RpcEndEvent;
import honeycrm.client.mvp.events.RpcEndEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.SimpleEventBus;

public class AppPresenter extends AbstractPresenter {
	public interface Display extends AbstractPresenterDisplay {
		HasKeyPressHandlers getFocus();
		void selectTab(String module);
		HasClickHandlers getLogout();
		void toggleLoading(boolean isLoading);
	}

	private int concurrentRpcs;

	public AppPresenter(final SimpleEventBus bus, final Display view) {
		this.view = view;
		this.concurrentRpcs = 0;

		view.toggleLoading(false);
		view.getLogout().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bus.fireEvent(new LogoutEvent());
			}
		});
		bus.addHandler(RpcBeginEvent.TYPE, new RpcBeginEventHandler() {
			@Override
			public void onRpcBegin(RpcBeginEvent event) {
				concurrentRpcs++; view.toggleLoading(true);
			}
		});
		bus.addHandler(RpcEndEvent.TYPE, new RpcEndEventHandler() {
			@Override
			public void onRpcEnd(RpcEndEvent event) {
				concurrentRpcs--; view.toggleLoading(concurrentRpcs == 0);
				
			}
		});
		bus.addHandler(OpenModuleEvent.TYPE, new OpenModuleEventHandler() {
			@Override
			public void onOpenModule(OpenModuleEvent event) {
				view.selectTab(event.getModule());
			}
		});
		view.getFocus().addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(final KeyPressEvent event) {
				GWT.runAsync(new RunAsyncCallback() {
					@Override
					public void onSuccess() {
						bus.fireEvent(new ShortcutEvent(event.getCharCode()));
					}

					@Override
					public void onFailure(Throwable reason) {
					}
				});
			}
		});
	}
}
