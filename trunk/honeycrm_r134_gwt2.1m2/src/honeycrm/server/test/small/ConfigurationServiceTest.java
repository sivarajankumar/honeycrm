package honeycrm.server.test.small;

import honeycrm.client.dto.Configuration;
import honeycrm.server.services.ConfigServiceImpl;
import junit.framework.TestCase;

public class ConfigurationServiceTest extends TestCase {
	private final ConfigServiceImpl configurator = new ConfigServiceImpl();

	public void testConfigurator() {
		final Configuration c = configurator.getConfiguration();
		
	}
}
