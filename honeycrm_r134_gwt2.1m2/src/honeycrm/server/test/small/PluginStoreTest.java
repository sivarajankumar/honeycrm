package honeycrm.server.test.small;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.client.Command;

import honeycrm.client.misc.PluginClassBytecode;
import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.Timer;
import honeycrm.server.PluginStore;

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
	
	public void testLoadPluginWithoutInstalledPlugins() {
		store.loadPlugin(null);
	}
	
	public void testLoadPluginWithTwoInstalledPlugins() throws FileNotFoundException, IOException {
		store.createPlugin(new PluginDescription("foo1", "wohoo"), new FileInputStream(FILE));
		store.createPlugin(new PluginDescription("foo2", "wohoo"), new FileInputStream(FILE2));
		store.loadPlugin("foo1");
	}
	
	public void testLoadNewlyCreatedPlugin() throws FileNotFoundException, IOException {
		store.createPlugin(new PluginDescription("foo1", "wohoo"), new FileInputStream(new File("AAA.jar")));
		store.loadPlugin("foo1");
	}
}
