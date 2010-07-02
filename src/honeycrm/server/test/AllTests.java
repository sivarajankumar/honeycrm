package honeycrm.server.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("Server code tests");
		// $JUnit-BEGIN$
		suite.addTestSuite(NumberParserTest.class);
		suite.addTestSuite(CacheTest.class);
		suite.addTestSuite(CollectionHelperTest.class);
		suite.addTestSuite(CsvExportTest.class);
		suite.addTestSuite(CsvImportTest.class);
		suite.addTestSuite(OfferingReportsTest.class);
		suite.addTestSuite(DemoDataProviderTest.class);
		suite.addTestSuite(DtoSyncTest.class);
		// $JUnit-END$
		return suite;
	}
}
