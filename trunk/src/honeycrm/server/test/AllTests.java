package honeycrm.server.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("Server code tests");
		// $JUnit-BEGIN$
		suite.addTestSuite(NumberParserTest.class);
		suite.addTestSuite(CollectionHelperTest.class);
		suite.addTestSuite(CsvExportTest.class);
		suite.addTestSuite(DemoDataProviderTest.class);
		suite.addTestSuite(ObjectCopyTest.class);
		suite.addTestSuite(DtoSyncTest.class);
		suite.addTestSuite(DtoIndicesTest.class);
		suite.addTestSuite(IANATest.class);
		// $JUnit-END$
		return suite;
	}
}
