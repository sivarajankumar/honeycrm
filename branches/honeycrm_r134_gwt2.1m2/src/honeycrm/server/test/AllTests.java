package honeycrm.server.test;

import honeycrm.server.transfer.ReflectionHelper;

import java.io.IOException;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() throws ClassNotFoundException, IOException {
		TestSuite suite = new TestSuite("All tests of honeycrm.server.test");
		/**
		 * add all tests in the honeycrm.server.test package to the test suite using reflection.
		 */
		for (final Class<?> testcase : ReflectionHelper.getClasses("honeycrm.server.test")) {
			if (Modifier.isAbstract(testcase.getModifiers()) || testcase.isAnonymousClass() || testcase.equals(AllTests.class)) {
				/**
				 * skip this class, abstract and anonymous classes (e.g. inner classes like Foo$1)
				 */
				continue;
			} else {
				suite.addTestSuite((Class<TestCase>) testcase);
			}
		}
		return suite;
	}
}
