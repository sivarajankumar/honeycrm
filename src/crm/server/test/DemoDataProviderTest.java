package crm.server.test;
import junit.framework.TestCase;
import crm.server.DemoDataProvider;

public class DemoDataProviderTest extends TestCase {
	public void testRandomData() {
		for (int i=0; i<999; i++) {
			DemoDataProvider.account();
			DemoDataProvider.contact();
			//DemoDataProvider.service();
			//DemoDataProvider.contract();
			//DemoDataProvider.user();
		}
	}
}
