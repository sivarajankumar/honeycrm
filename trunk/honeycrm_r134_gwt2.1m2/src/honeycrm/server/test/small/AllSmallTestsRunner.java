package honeycrm.server.test.small;

import java.io.IOException;

import junit.framework.Test;
import honeycrm.server.test.AllTestsRunner;

public class AllSmallTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.small");
	}
}
