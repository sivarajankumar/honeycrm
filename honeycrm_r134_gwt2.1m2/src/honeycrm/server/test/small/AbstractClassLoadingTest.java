package honeycrm.server.test.small;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import honeycrm.server.PluginStore;
import honeycrm.server.test.medium.DatastoreTest;

abstract public class AbstractClassLoadingTest extends DatastoreTest {
	protected static final File FILE = new File("dynamicLoadingTesting.jar");
	protected static final File FILE2 = new File("dynamicLoadingTestingTwoFiles.jar");
	protected DatastoreService db;
	protected PluginStore store;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = DatastoreServiceFactory.getDatastoreService();
		store = new PluginStore();
	}

	protected byte[] getBytesOfFile(final File file) throws FileNotFoundException, IOException {
		byte[] bytes = new byte[8096];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		while (in.read(bytes) > 0) {
			out.write(bytes);
		}

		return out.toByteArray();
	}
}
