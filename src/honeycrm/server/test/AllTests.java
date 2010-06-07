package honeycrm.server.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for honeycrm.server.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(ArrayHelperTest.class);
		suite.addTestSuite(DemoDataProviderTest.class);
		suite.addTestSuite(ObjectCopyTest.class);
		suite.addTestSuite(DtoSyncTest.class);
		suite.addTestSuite(DtoIndicesTest.class);
		suite.addTestSuite(IANATest.class);
		// $JUnit-END$
		return suite;
	}

}
