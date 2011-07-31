package honeycrm.server.test.small;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.plugin.IPlatform;
import honeycrm.server.PluginStore;
import honeycrm.server.services.PluginServiceImpl;
import honeycrm.server.test.small.mocks.PlatformMock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PluginServiceTest extends AbstractClassLoadingTest {
	private final PluginServiceImpl pluginService = new PluginServiceImpl();
	private IPlatform app = new PlatformMock();

	public void testGettingPlugins() {
		final AbstractPlugin[] plugins = pluginService.getAvailablePlugins();

		assertNotNull(plugins);
		assertTrue(0 < plugins.length);

		for (final AbstractPlugin plugin : plugins) {
			plugin.initialize(); // app/*, null*/);
			// plugin.runPlugin();
		}
	}

	public void testGetPluginsAfterAddingNewPlugin() throws FileNotFoundException, IOException {
		final int pluginCount = pluginService.getPluginDescriptions().length;

		final PluginStore store = new PluginStore();
		store.createPlugin(new PluginDescription("foo", "new one"), new FileInputStream(FILE2));

		assertEquals(1 + pluginCount, pluginService.getPluginDescriptions().length);
	}
}
