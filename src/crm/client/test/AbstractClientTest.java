package crm.client.test;

import com.google.gwt.junit.client.GWTTestCase;

abstract public class AbstractClientTest extends GWTTestCase {
	@Override
	public String getModuleName() {
		return "crm.Gae";
	}
}
