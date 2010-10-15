package platform.client;

import java.util.HashSet;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

public class PluginRegistry implements Exportable {
	private static final HashSet<AbstractPlugin> plugins = new HashSet<AbstractPlugin>();

	@Export
	// Only (!) export this method.
	public static void registerPluginObject(final AbstractPlugin plugin) {
		plugins.add(plugin);
	}
	
	public static HashSet<AbstractPlugin> getPlugins() {
		return plugins;
	}
}
