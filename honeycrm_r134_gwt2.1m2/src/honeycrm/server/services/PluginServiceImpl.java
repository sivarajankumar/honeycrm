package honeycrm.server.services;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.services.PluginService;
import honeycrm.server.ReflectionHelper;

public class PluginServiceImpl extends RemoteServiceServlet implements PluginService {
	private static final long serialVersionUID = -5770355812567630413L;
	private static final Logger log = Logger.getLogger(PluginServiceImpl.class.getSimpleName());
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
}
