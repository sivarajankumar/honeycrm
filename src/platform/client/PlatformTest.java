package platform.client;

import platform.client.PluginRegistry;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class PlatformTest implements EntryPoint {
	@Override
	public void onModuleLoad() {
		GWT.create(PluginRegistry.class);
		RootLayoutPanel.get().add(new PluginPresenter());
	}
}
