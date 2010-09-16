package honeycrm.server.test;

import java.util.Random;

import honeycrm.server.CommonServiceImpl;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import junit.framework.TestCase;

public abstract class DatastoreTest extends TestCase {
	protected static final Random random = new Random(System.currentTimeMillis());
	protected final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	protected CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}
}