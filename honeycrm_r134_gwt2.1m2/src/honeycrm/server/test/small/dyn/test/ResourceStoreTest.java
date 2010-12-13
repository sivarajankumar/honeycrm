package honeycrm.server.test.small.dyn.test;

import java.io.FileInputStream;
import java.util.Arrays;

import honeycrm.server.test.small.dyn.hotreload.ResourceStore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class ResourceStoreTest extends AbstractClassLoadingTest {
	private DatastoreService db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = DatastoreServiceFactory.getDatastoreService();
	}

	public void testSimplePut() {
		ResourceStore r = new ResourceStore(db, "Classes");

		final byte[] bytes = new byte[] { 1 };

		r.put("foo/bar", bytes);
		assertTrue(Arrays.equals(bytes, r.get("foo/bar")));
	}

	public void testPutJarFile() {
		try {
			ResourceStore r = new ResourceStore(db, "Classes");
			r.put(store.getBytecodeMapFromJarInputStream(new FileInputStream(FILE)));
			assertNotNull(r.get("honeycrm.server.test.small.DynamicallyLoadedClass"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
