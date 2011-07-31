package honeycrm.server.test.small;

import honeycrm.server.test.AllTestsRunner;

import java.io.IOException;

import junit.framework.Test;

public class AllSmallTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.small");
	}
}
