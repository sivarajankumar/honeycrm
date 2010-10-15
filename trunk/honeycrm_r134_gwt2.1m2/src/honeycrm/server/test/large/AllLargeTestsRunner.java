package honeycrm.server.test.large;

import java.io.IOException;

import honeycrm.server.test.AllTestsRunner;
import junit.framework.Test;

public class AllLargeTestsRunner extends AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test.large");
	}
}
