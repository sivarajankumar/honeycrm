package plugin.client;

import java.util.HashSet;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

@Export
public class PluginRegistry implements Exportable {
	private static final HashSet<String> plugins = new HashSet<String>();

	public static void registerPlugin(final String pluginName) {
		plugins.add(pluginName);
	}

	public static String getPlugins() {
		String pluginString = "";
		for (final String plugin : plugins) {
			pluginString += plugin + " ";
		}
		return pluginString;
	}
}
