package plugin.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class PluginPresenter extends Composite { 
	public PluginPresenter() {
		final Label pluginLabel = new Label("no plugins loaded");
		
		new Timer() {
			@Override
			public void run() {
				pluginLabel.setText(PluginRegistry.getPlugins());
			}
		}.scheduleRepeating(100);
		
		initWidget(pluginLabel);
	}
}
