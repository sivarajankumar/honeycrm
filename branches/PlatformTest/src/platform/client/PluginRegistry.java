package platform.client;

import java.util.HashSet;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

public class PluginRegistry implements Exportable {
	private static final HashSet<AbstractPlugin> plugins = new HashSet<AbstractPlugin>();

	@Export
	//
	// Only (!) export this method.
	//
	// This only has to be exported if the plugins are compiled separately
	// If they are compiled as part of the platform we can use usual JSNI without the GWT-Exporter.
	// The AbstractPlugin.loadNewPluginObject code then would have to be changed to the syntax described on
	// http://code.google.com/intl/de-DE/webtoolkit/doc/latest/DevGuideCodingBasicsJSNI.html#methods-fields
	//
	// @<package>.<class>::<staticMethod> ...
	//
	public static void registerPluginObject(final AbstractPlugin plugin) {
		plugins.add(plugin);
	}
	
	public static HashSet<AbstractPlugin> getPlugins() {
		return plugins;
	}
}
