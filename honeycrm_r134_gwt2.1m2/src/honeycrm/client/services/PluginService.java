package honeycrm.client.services;

import honeycrm.client.plugin.AbstractPlugin;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("plugin")
public interface PluginService extends RemoteService {
	String getPluginSource();
	AbstractPlugin[] getAvailablePlugins();
}
