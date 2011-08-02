package honeycrm.client.s;

import honeycrm.client.mvp.events.OpenModuleEvent;
import honeycrm.client.mvp.events.OpenModuleEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.SimpleEventBus;

public class AppPresenter extends AbstractPresenter {
	public interface Display extends AbstractPresenterDisplay {
		HasKeyPressHandlers getFocus();
		void selectTab(String module);
	}

	public AppPresenter(final SimpleEventBus bus, final Display view) {
		this.view = view;

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
