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
	@Override
	public void onModuleLoad() {
		final LoginScreen login = new LoginScreen(new Callback() {
			@Override
			public void callback() {
				// insert a code splitting point here
				GWT.runAsync(new RunAsyncCallback() {
					@Override
					public void onSuccess() {
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
