package honeycrm.client.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class AllUiTests extends GWTTestSuite {
	public static Test suite() {
		final TestSuite suite = new TestSuite("User interface tests");
		suite.addTestSuite(ServiceTableTest.class);
		suite.addTestSuite(FieldCurrencyTest.class);
		suite.addTestSuite(ListViewTest.class);
		return suite;
	}
}
