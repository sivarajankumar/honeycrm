package honeycrm.server.test.small.dyn.test;

import honeycrm.server.test.Plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

public class DynamicClassLoadingTest extends TestCase {
	public void testLoadingFromJarFile() {
		try {
			URLClassLoader cl = new URLClassLoader(new URL[] { new File("dynamicLoadingTesting.jar").toURL() });

			Class<?> c = cl.loadClass("honeycrm.server.test.small.DynamicallyLoadedClass");

			assertTrue(c.getInterfaces()[0].equals(Plugin.class));

			Plugin p = (Plugin) c.newInstance();
			System.out.println("request answer = " + p.request());
		} catch (Exception e) {
			fail();
		}
	}
}
