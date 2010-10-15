package honeycrm.client;

import honeycrm.client.basiclayout.Initializer;
import honeycrm.client.login.LoginScreen;
import honeycrm.client.misc.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	private native void loadPlugin(final String code) /*-{
		var d = document.createElement('div');
		d.innerHTML = 'attached plugin';
		@honeycrm.client.Header::attach(Lcom/google/gwt/dom/client/Element;)(d);
		// eval(code);
	}-*/;

	@Override
	public void onModuleLoad() {
		/*
		 * RootLayoutPanel.get().add(Header.Instance); ServiceRegistry.pluginService().getPluginSource(new AsyncCallback<String>() {
		 * 
		 * @Override public void onSuccess(String result) { loadPlugin(result); }
		 * 
		 * @Override public void onFailure(Throwable caught) { Window.alert("Could not load plugin"); } });
		 */

		final LoginScreen login = new LoginScreen(new Callback() {
			@Override
			public void callback() {
				// insert a code splitting point here
				GWT.runAsync(new RunAsyncCallback() {
					@Override
					public void onSuccess() {
						// capture key codes like the following:
						//
						// final FocusPanel focus = new FocusPanel(new Initializer());
						// focus.addKeyDownHandler(new KeyDownHandler() { //
						// @Override public void onKeyDown(KeyDownEvent event) { if (event.isShiftKeyDown() && event.getNativeKeyCode() == 16) { // == ?) { LogConsole.log("display help"); } } }); focus.setHeight("100%"); RootLayoutPanel.get().add(focus);
						RootLayoutPanel.get().add(new Initializer());
					}

					@Override
					public void onFailure(Throwable reason) {
						Window.alert("Could not additional honeycrm code for initialisation");
					}
				});
			}
		});
		RootLayoutPanel.get().add(login);
		login.center();

	}
}
