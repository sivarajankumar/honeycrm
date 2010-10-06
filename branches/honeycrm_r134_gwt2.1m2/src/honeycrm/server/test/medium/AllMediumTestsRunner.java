package honeycrm.server.test.medium;

import java.io.IOException;

import honeycrm.server.test.AllTestsRunner;
import junit.framework.Test;

public class AllMediumTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.medium");
	}
}
