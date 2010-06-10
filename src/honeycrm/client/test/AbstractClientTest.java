package honeycrm.client.test;

import honeycrm.client.ServiceRegistry;

import com.google.gwt.junit.client.GWTTestCase;

// TODO still have to inherit a missing module for gwttestcase (?). otherise client code won't work
// properly (?).
abstract public class AbstractClientTest extends GWTTestCase {
	static {
		ServiceRegistry.injectCommonService(new CommonServiceTestHelper());
	}

	@Override
	public String getModuleName() {
		return "honeycrm.Gae";
	}
}
