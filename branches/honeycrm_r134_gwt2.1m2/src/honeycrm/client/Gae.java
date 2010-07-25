package honeycrm.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Gae implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new LoadingPanel());
	}
}
