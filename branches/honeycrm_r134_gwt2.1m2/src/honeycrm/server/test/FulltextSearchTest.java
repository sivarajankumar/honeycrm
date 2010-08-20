package honeycrm.server.test;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.Timer;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gwt.user.client.Command;

public class FulltextSearchTest extends TestCase {
	private static final int ENTITY_COUNT = 1000;
	private static final int SEARCH_ITERATIONS = 100;
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private CommonServiceImpl commonService;
	private final DtoCopyMachine copy = new DtoCopyMachine();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
		commonService = new CommonServiceImpl();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}

	public void testSearch() {
		final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		createEntities();

		for (int i = 0; i < SEARCH_ITERATIONS; i++) {
			final long time = Timer.getTime(new Command() {
				@Override
				public void execute() {
					search();
				}
			});
			System.out.println("#" + (1+i) + " fulltext search in " + ENTITY_COUNT + " accounts & contacts took " + time + " ms.");
		}
	}

	private void search() {
		final ListQueryResult result = commonService.fulltextSearch("Simpson", 0, 20);
		assertNotNull(result.getResults());
//		assertTrue(result.getResults().length > 0);
	}

	private void createEntities() {
		final long time = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < ENTITY_COUNT; i++) {
					commonService.create(copy.copy(DemoDataProvider.contact()));
					commonService.create(copy.copy(DemoDataProvider.account()));
				}
			}
		});
		System.out.println("Creating " + ENTITY_COUNT * 2 + " entities took " + time + " ms");
	}
}
