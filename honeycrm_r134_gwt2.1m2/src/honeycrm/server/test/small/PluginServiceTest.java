package honeycrm.server.test.small;

import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.plugin.IPlatform;
import honeycrm.server.services.PluginServiceImpl;
import honeycrm.server.test.small.mocks.PlatformMock;
import junit.framework.TestCase;

public class PluginServiceTest extends TestCase {
	private final PluginServiceImpl pluginService = new PluginServiceImpl();
	private  IPlatform app = new PlatformMock();
	
	public void testGettingPlugins() {
		final AbstractPlugin[] plugins = pluginService.getAvailablePlugins();
		
		assertNotNull(plugins);
		assertTrue(0 < plugins.length);
		
		for (final AbstractPlugin plugin: plugins  ) {
			plugin.initialize(app/*, null*/);
			plugin.runPlugin();
		}
	}
}
