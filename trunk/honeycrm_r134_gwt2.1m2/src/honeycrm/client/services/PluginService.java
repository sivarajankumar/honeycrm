package honeycrm.client.services;

import java.util.ArrayList;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.PluginRequest;
import honeycrm.client.misc.PluginResponse;
import honeycrm.client.plugin.AbstractPlugin;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("plugin")
public interface PluginService extends RemoteService {
	String getPluginSource();
	AbstractPlugin[] getAvailablePlugins();
	ArrayList<String> getPluginNames();
	PluginDescription[] getPluginDescriptions();
	PluginResponse request(PluginRequest request);
}
