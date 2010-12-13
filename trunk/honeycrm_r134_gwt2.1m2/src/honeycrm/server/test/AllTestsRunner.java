package honeycrm.server.test;

import honeycrm.server.ReflectionHelper;
import honeycrm.server.test.large.AllLargeTestsRunner;
import honeycrm.server.test.medium.AllMediumTestsRunner;
import honeycrm.server.test.small.AllSmallTestsRunner;

import java.io.IOException;
import java.lang.reflect.Modifier;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTestsRunner {
	public static Test suite() throws ClassNotFoundException, IOException {
		return getTestSuite("honeycrm.server.test");
	}

	protected static Test getTestSuite(final String packageName) throws ClassNotFoundException, IOException {
		TestSuite suite = new TestSuite("All tests of " + packageName);

		for (final Class<?> testcase : ReflectionHelper.getClasses(packageName)) {
			try {
				if (Modifier.isAbstract(testcase.getModifiers()) || testcase.isAnonymousClass() || testcase.equals(AllMediumTestsRunner.class) || testcase.equals(AllTestsRunner.class) || testcase.equals(AllSmallTestsRunner.class) || testcase.equals(AllLargeTestsRunner.class)) {
					/**
					 * skip this class, abstract and anonymous classes (e.g. inner classes like Foo$1)
					 */
					continue;
				} else if (testcase.newInstance() instanceof TestCase) {
					suite.addTestSuite((Class<TestCase>) testcase);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return suite;
	}
}