package honeycrm.server.test.medium;

import honeycrm.server.test.AllTestsRunner;

import java.io.IOException;

import junit.framework.Test;

public class AllMediumTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.medium");
	}
}
