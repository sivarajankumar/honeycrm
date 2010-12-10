package honeycrm.server.test.small.dyn.test;

import java.util.Arrays;
import java.util.HashMap;

import honeycrm.server.test.small.dyn.ResourceStore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class MyResourceStoreTest extends AbstractClassLoadingTest {
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
			r.put(getBytecodeMapFromJarInputStream(FILE));
			assertNotNull(r.get("honeycrm/server/test/small/DynamicallyLoadedClass.class"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
