package honeycrm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Gae implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new LoadingPanel());
	}
}
