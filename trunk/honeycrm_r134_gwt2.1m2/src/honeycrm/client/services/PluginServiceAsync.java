package honeycrm.client.services;

import java.util.ArrayList;

import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.server.test.small.dyn.PluginDescription;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PluginServiceAsync {
	void getPluginSource(AsyncCallback<String> callback);
	void getAvailablePlugins(AsyncCallback<AbstractPlugin[]> callback);
	void getPluginNames(AsyncCallback<ArrayList<String>> callback);
	void getPluginDescriptions(AsyncCallback<PluginDescription[]> callback);
}
