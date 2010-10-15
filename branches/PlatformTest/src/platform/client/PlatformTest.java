package platform.client;

import platform.client.PluginRegistry;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlatformTest implements EntryPoint {
	@Override
	public void onModuleLoad() {
		GWT.create(PluginRegistry.class);
		
		final VerticalPanel p = new VerticalPanel();
		p.add(new PluginPresenter());
		p.add(new Label("The rest of the Web Application could sit here."));

		RootLayoutPanel.get().add(p);
	}
}
