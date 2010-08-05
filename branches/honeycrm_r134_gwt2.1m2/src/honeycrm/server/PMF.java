package honeycrm.server;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

// import org.compass.core.Compass;
// import org.compass.core.config.CompassConfiguration;
// import org.compass.core.config.CompassEnvironment;
// import org.compass.gps.CompassGps;
// import org.compass.gps.device.jdo.Jdo2GpsDevice;
// import org.compass.gps.impl.SingleCompassGps;

// TODO disabled compass / lucene until it works when deployed on appengine
final public class PMF {
	private static final PersistenceManagerFactory PMF;
	// private static final Compass compass;
	// private static final CompassGps compassGps;

	static {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
		map.put("javax.jdo.option.ConnectionURL", "appengine");
		map.put("javax.jdo.option.NontransactionalRead", "true");
		map.put("javax.jdo.option.NontransactionalWrite", "true");
		map.put("javax.jdo.option.RetainValues", "true");
		map.put("datanucleus.appengine.autoCreateDatastoreTxns", "true");

		PMF = JDOHelper.getPersistenceManagerFactory(map);

		// pmf =
		// JDOHelper.getPersistenceManagerFactory("transactions-optional");

		// compass = new CompassConfiguration()
		// .setConnection("gae://index")
		// .setSetting(CompassEnvironment.ExecutorManager.EXECUTOR_MANAGER_TYPE,
		// "disabled")
		// .addScan("honeycrm.server.domain")
		// .buildCompass();

		// compassGps = new SingleCompassGps(compass);
		// compassGps.addGpsDevice(new Jdo2GpsDevice("appengine", pmf));
		// compassGps.start();
		// compassGps.index();
	}

	public static PersistenceManagerFactory get() {
		return PMF;
	}

	// public static Compass compass() {
	// return compass;
	// }

	private PMF() {
	}
}
