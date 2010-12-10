package honeycrm.server.test.small.dyn;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class PluginStore {
	final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	
	public void createPlugin(final PluginDescription plugin, final InputStream is) throws IOException {
		final ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.add(getPluginDescriptionEntity(plugin));
		
		for (final Map.Entry<String, byte[]> entry: getBytecodeMapFromJarInputStream(is).entrySet()) {
			final Entity pluginBytecode = new Entity(PluginClassBytecode.class.getSimpleName());
			
			pluginBytecode.setProperty("className", entry.getKey());
			pluginBytecode.setProperty("bytecode", new Blob(entry.getValue()));
			
			entities.add(pluginBytecode);
		}
		
		db.put(entities);
	}

	public Entity getPluginDescriptionEntity(final PluginDescription plugin) {
		final Entity pluginDescription = new Entity(PluginDescription.class.getSimpleName());
		pluginDescription.setProperty("name", plugin.getName());
		pluginDescription.setProperty("description", plugin.getDescription());
		return pluginDescription;
	}

	public HashMap<String, byte[]> getBytecodeMapFromJarInputStream(final InputStream is) throws IOException {
		final HashMap<String, byte[]> map = new HashMap<String, byte[]>();
		final JarInputStream in = new JarInputStream(is);
		
		JarEntry e;
		while (null != (e = in.getNextJarEntry())) {
			System.out.println("at entry: " + e.getName());

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			int bytecode;
			
			while ((bytecode = in.read()) != -1) {
				out.write(bytecode);
			}

			map.put(e.getName(), out.toByteArray());
		}
		return map;
	}
}
