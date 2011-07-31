package honeycrm.server.test.large;

import honeycrm.server.test.AllTestsRunner;

import java.io.IOException;

import junit.framework.Test;

public class AllLargeTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.large");
	}
}
