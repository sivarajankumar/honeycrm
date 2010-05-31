package crm.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

//import org.compass.core.Compass;
//import org.compass.core.config.CompassConfiguration;
//import org.compass.core.config.CompassEnvironment;
//import org.compass.gps.CompassGps;
//import org.compass.gps.device.jdo.Jdo2GpsDevice;
//import org.compass.gps.impl.SingleCompassGps;

// TODO disabled compass / lucene until it works when deployed on appengine
public class PMF {
	private static final PersistenceManagerFactory pmf;
	// private static final Compass compass;
	// private static final CompassGps compassGps;

	static {
		pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

		// compass = new CompassConfiguration()
		// .setConnection("gae://index")
		// .setSetting(CompassEnvironment.ExecutorManager.EXECUTOR_MANAGER_TYPE, "disabled")
		// .addScan("crm.server.domain")
		// .buildCompass();

		// compassGps = new SingleCompassGps(compass);
		// compassGps.addGpsDevice(new Jdo2GpsDevice("appengine", pmf));
		// compassGps.start();
		// compassGps.index();
	}

	public static PersistenceManagerFactory get() {
		return pmf;
	}

	// public static Compass compass() {
	// return compass;
	// }

	private PMF() {
	}
}
