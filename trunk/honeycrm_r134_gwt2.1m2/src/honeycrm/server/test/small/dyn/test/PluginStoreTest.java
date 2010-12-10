package honeycrm.server.test.small.dyn.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.client.Command;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.Timer;
import honeycrm.server.test.small.dyn.PluginClassBytecode;
import honeycrm.server.test.small.dyn.PluginStore;

public class PluginStoreTest extends AbstractClassLoadingTest {
	private PluginStore store;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		store = new PluginStore();
	}

	public void testGetPluginDescription() {
		assertNotNull(store.getPluginDescriptionEntity(new PluginDescription("foo", "bar")));
	}

	public void testCreatePluginAddsPluginDescriptionEntity() throws FileNotFoundException, IOException {
		store.createPlugin(new PluginDescription("foo", "wohoo"), new FileInputStream(FILE2));
		final PreparedQuery pq = db.prepare(new Query(PluginDescription.class.getSimpleName()));
		assertEquals(1, pq.countEntities());
	}

	public void testCreatePluginAddsTwoBytecodeEntities() throws FileNotFoundException, IOException {
		store.createPlugin(new PluginDescription("foo", "wohoo"), new FileInputStream(FILE2));
		final PreparedQuery pq = db.prepare(new Query(PluginClassBytecode.class.getSimpleName()));
		assertEquals(2, pq.countEntities());
	}

	public void testCreatePluginCallInLessThanTenMilliseconds() throws IOException {
		final double count = 1000;
		final PluginDescription d = new PluginDescription("foo", "bar");
		final InputStream is = new FileInputStream(FILE2);

		final long time = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					try {
						store.createPlugin(d, is);
					} catch (IOException e) {
						fail();
					}
				}
			}
		});

		final double averageTimePerCall = time / count;
		assertTrue(averageTimePerCall < 10);
	}
}
