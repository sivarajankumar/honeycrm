package honeycrm.server.test.small.dyn;

import honeycrm.client.misc.PluginDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class PluginStore {
	private static final Logger log = Logger.getLogger(PluginStore.class.getSimpleName());
	private static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	private final FooClassLoader loader = new FooClassLoader();

	public void loadPlugin(final String pluginName) {
		// TODO do this only for the given plugin

		for (final Entity pluginBytecode : db.prepare(new Query(PluginClassBytecode.class.getSimpleName())).asIterable()) {
			final String className = String.valueOf(pluginBytecode.getProperty("className"));
			final Blob bytecode = (Blob) pluginBytecode.getProperty("bytecode");

			try {
				loader.defineClass(className, bytecode.getBytes());
			} catch (ClassFormatError e) {
				log.warning("Could not load class " + className + ". A ClassFormatError occured.");
			} catch (LinkageError e) {
				log.warning("LinkageError while trying to load " + className);
			}
		}
	}

	public void createPlugin(final PluginDescription plugin, final InputStream is) throws IOException {
		final ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.add(getPluginDescriptionEntity(plugin));

		for (final Map.Entry<String, byte[]> entry : getBytecodeMapFromJarInputStream(is).entrySet()) {
			final Entity pluginBytecode = new Entity(PluginClassBytecode.class.getSimpleName());

			pluginBytecode.setProperty("className", entry.getKey());
			pluginBytecode.setProperty("bytecode", new Blob(entry.getValue()));

			entities.add(pluginBytecode);

			System.out.println("class name = " + entry.getKey());
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
			final String sanatizedClassName = e.getName().replace("/", ".").replace(".class", "");

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			int bytecode;

			while ((bytecode = in.read()) != -1) {
				out.write(bytecode);
			}

			map.put(sanatizedClassName, out.toByteArray());
		}
		return map;
	}

	class FooClassLoader extends ClassLoader {
		public void defineClass(String className, byte[] bytecode) throws ClassFormatError {
			try {
				// try to resolve this class
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// define this class since it could not be resolved yet.
				super.defineClass(className, bytecode, 0, bytecode.length);
			}
		}
	}
}
