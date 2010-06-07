package honeycrm.client.test;

import com.google.gwt.junit.client.GWTTestCase;

// TODO still have to inherit a missing module for gwttestcase (?). otherise client code won't work
// properly (?).
abstract public class AbstractClientTest extends GWTTestCase {
	@Override
	public String getModuleName() {
		return "honeycrm.Gae";
	}
}
