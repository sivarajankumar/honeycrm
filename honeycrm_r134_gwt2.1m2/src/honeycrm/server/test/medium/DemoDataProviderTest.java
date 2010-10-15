package honeycrm.server.test.medium;

import honeycrm.server.DemoDataProvider;
import junit.framework.TestCase;

public class DemoDataProviderTest extends TestCase {
	public void testRandomData() {
		for (int i = 0; i < 999; i++) {
			DemoDataProvider.account();
			DemoDataProvider.contact();
			// DemoDataProvider.service();
			// DemoDataProvider.contract();
			// DemoDataProvider.user();
		}
	}
}
