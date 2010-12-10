package honeycrm.client.services;

import java.util.ArrayList;

import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.server.test.small.dyn.PluginDescription;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("plugin")
public interface PluginService extends RemoteService {
	String getPluginSource();
	AbstractPlugin[] getAvailablePlugins();
	ArrayList<String> getPluginNames();
	PluginDescription[] getPluginDescriptions();
}
