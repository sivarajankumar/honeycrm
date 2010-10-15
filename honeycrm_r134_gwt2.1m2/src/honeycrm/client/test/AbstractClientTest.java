package honeycrm.client.test;

import com.google.gwt.junit.client.GWTTestCase;

abstract public class AbstractClientTest extends GWTTestCase {
	static {
		// TODO get client side tests working again and create service instances if it works again.
		// ServiceRegistry.injectCommonService(new CommonServiceTestHelper());
	}

	@Override
	public String getModuleName() {
		return "honeycrm.Gae";
	}
}
