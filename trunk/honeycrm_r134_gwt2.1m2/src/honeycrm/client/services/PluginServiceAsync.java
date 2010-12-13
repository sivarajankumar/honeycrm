package honeycrm.client.services;

import java.util.ArrayList;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.PluginRequest;
import honeycrm.client.misc.PluginResponse;
import honeycrm.client.plugin.AbstractPlugin;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PluginServiceAsync {
	void getPluginSource(AsyncCallback<String> callback);
	void getAvailablePlugins(AsyncCallback<AbstractPlugin[]> callback);
	void getPluginNames(AsyncCallback<ArrayList<String>> callback);
	void getPluginDescriptions(AsyncCallback<PluginDescription[]> callback);
	void request(PluginRequest request, AsyncCallback<PluginResponse> callback);
}
