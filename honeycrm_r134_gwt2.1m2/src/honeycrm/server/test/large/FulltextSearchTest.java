package honeycrm.server.test.large;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.Timer;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.test.medium.DatastoreTest;
import com.google.gwt.user.client.Command;

public class FulltextSearchTest extends DatastoreTest {
	private static final int ENTITY_COUNT = 1000;
	private static final int SEARCH_ITERATIONS = 100;

	public void testSearch() {
		createEntities();

		for (int i = 0; i < SEARCH_ITERATIONS; i++) {
			final long time = Timer.getTime(new Command() {
				@Override
				public void execute() {
					search();
				}
			});
			System.out.println("#" + (1 + i) + " fulltext search in " + ENTITY_COUNT + " accounts & contacts took " + time + " ms.");
		}
	}

	private void search() {
		final ListQueryResult result = readService.fulltextSearch("Simpson", 0, 20);
		assertNotNull(result.getResults());
		// assertTrue(result.getResults().length > 0);
	}

	private void createEntities() {
		final long time = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < ENTITY_COUNT; i++) {
					createService.create((DemoDataProvider.contact()));
					createService.create((DemoDataProvider.account()));
				}
			}
		});
		System.out.println("Creating " + ENTITY_COUNT * 2 + " entities took " + time + " ms");
	}
}
