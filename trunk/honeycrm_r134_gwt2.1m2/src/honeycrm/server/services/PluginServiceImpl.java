package honeycrm.server.services;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.PluginRequest;
import honeycrm.client.misc.PluginResponse;
import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.services.PluginService;
import honeycrm.server.PluginStore;
import honeycrm.server.ReflectionHelper;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PluginServiceImpl extends RemoteServiceServlet implements PluginService {
	private static final long serialVersionUID = -5770355812567630413L;
	private static final Logger log = Logger.getLogger(PluginServiceImpl.class.getSimpleName());
	private static final PluginStore store = new PluginStore();
	protected static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public String getPluginSource() {
		return "alert('hello world');";
	}

	@Override
	public AbstractPlugin[] getAvailablePlugins() {
		final ArrayList<AbstractPlugin> list = new ArrayList<AbstractPlugin>();

		try {
			for (final Class<?> clazz : ReflectionHelper.getClassesWithSuperclass("honeycrm.client.plugin", AbstractPlugin.class)) {
				list.add((AbstractPlugin) clazz.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Exception '" + e.toString() + "' occured while instantiating available plugins.");
			throw new RuntimeException("Exception '" + e.toString() + "' occured while instantiating available plugins.");
		}

		return list.toArray(new AbstractPlugin[0]);
	}

	@Override
	public ArrayList<String> getPluginNames() {
		final ArrayList<String> list = new ArrayList<String>();
		final PreparedQuery q = db.prepare(new Query("Plugin"));
		for (final Entity e: q.asIterable()) {
			list.add(String.valueOf(e.getProperty("name")));
		}
		return list;
	}

	@Override
	public PluginDescription[] getPluginDescriptions() {
		final ArrayList<PluginDescription> list = new ArrayList<PluginDescription>();
	
		for (final Entity plugin: db.prepare(new Query(PluginDescription.class.getSimpleName())).asIterable()) {
			final String name = String.valueOf(plugin.getProperty("name"));
			final String description = String.valueOf(plugin.getProperty("description"));
			list.add(new PluginDescription(name, description));
		}
		
		return list.toArray(new PluginDescription[0]);
	}

	@Override
	public PluginResponse request(PluginRequest request) {
		// final String pluginName = request.getDescription().getName();
		// final Query q = new Query(PluginDescription.class.getSimpleName());
		// q.addFilter("name", FilterOperator.EQUAL, pluginName);
		
		// final PreparedQuery pq = db.prepare(q);
		
		// if (0 == pq.countEntities()) {
		// 	return new PluginResponse("none");
		// } else {
			// assume class is already laoded
			final boolean classAlreadyLoaded = true;
			
			if (classAlreadyLoaded) {
				try {
					// TODO instantiate the class for this plugin
					// TODO which class should be loaded for this plugin - need to know the main class for this plugin e.g. via manifest definition.
					
					// TODO request is not among the methods..
					ClassLoader loader = store.loadPlugin("foo");
					
					final Class c = Class.forName("honeycrm.server.test.small.dyn.A", true, loader);
					final String returnValue = String.valueOf(c.getMethod("request").invoke(c.newInstance()));
					return new PluginResponse(returnValue);
				} catch (Exception e) {
					e.printStackTrace();
					return new PluginResponse("Exception during class loading " + e.getMessage());
				}
			} else {
				// load class from datastore first.
				return new PluginResponse("not implemented yet");
			}
			// }
	}
}
