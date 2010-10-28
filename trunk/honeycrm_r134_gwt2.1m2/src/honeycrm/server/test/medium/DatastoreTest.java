package honeycrm.server.test.medium;

import honeycrm.server.services.CreateServiceImpl;
import honeycrm.server.services.DeleteServiceImpl;
import honeycrm.server.services.ReadServiceImpl;
import honeycrm.server.services.UpdateServiceImpl;

import java.util.Random;

import junit.framework.TestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public abstract class DatastoreTest extends TestCase {
	protected static final Random random = new Random(System.currentTimeMillis());
	protected final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	// protected CommonServiceImpl commonService = new CommonServiceImpl();
	protected CreateServiceImpl createService = new CreateServiceImpl();
	protected ReadServiceImpl readService = new ReadServiceImpl();
	protected UpdateServiceImpl updateService = new UpdateServiceImpl();
	protected DeleteServiceImpl deleteService = new DeleteServiceImpl();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}
}
