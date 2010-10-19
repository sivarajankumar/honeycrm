package honeycrm.client.services;

import honeycrm.client.plugin.AbstractPlugin;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PluginServiceAsync {
	void getPluginSource(AsyncCallback<String> callback);
	void getAvailablePlugins(AsyncCallback<AbstractPlugin[]> callback);
}
