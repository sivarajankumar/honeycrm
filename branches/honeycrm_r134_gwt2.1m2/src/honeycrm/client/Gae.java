package honeycrm.client;

import honeycrm.client.basiclayout.Initializer;
import honeycrm.client.login.LoginScreen;
import honeycrm.client.misc.Callback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	@Override
	public void onModuleLoad() {
		final LoginScreen login = new LoginScreen(new Callback() {
			@Override
			public void callback() {
				RootLayoutPanel.get().add(new Initializer());
			}
		});
		RootLayoutPanel.get().add(login);
		login.center();
	}
}
