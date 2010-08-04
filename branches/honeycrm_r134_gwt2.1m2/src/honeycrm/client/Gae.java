package honeycrm.client;

import honeycrm.client.basiclayout.Initializer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new Initializer());
	}
}
