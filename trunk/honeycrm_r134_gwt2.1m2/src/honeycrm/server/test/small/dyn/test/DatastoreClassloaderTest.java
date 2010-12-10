package honeycrm.server.test.small.dyn.test;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.regex.Pattern;

import honeycrm.server.test.small.dyn.hotreload.DatastoreClassLoaderDelegate;
import honeycrm.server.test.small.dyn.hotreload.InterceptClassLoader;
import honeycrm.server.test.small.dyn.hotreload.ResourceStore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class DatastoreClassloaderTest extends AbstractClassLoadingTest {
	private static final String PATTERN = "honeycrm/server/.*";
	private DatastoreService db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = DatastoreServiceFactory.getDatastoreService();
	}
	
	public void testPattern() {
		Pattern p = Pattern.compile(PATTERN);
		assertTrue(p.matcher("honeycrm/server/test/small/DynamicallyLoadedClass.class").matches());
	}

	public void testDynamicLoadingFromJarFileWithOneClass() {
		try {
			ResourceStore r = new ResourceStore(db, "Classes");
			r.put(store.getBytecodeMapFromJarInputStream(new FileInputStream(FILE)));
			
			InterceptClassLoader loader = new InterceptClassLoader(getClass().getClassLoader(), Pattern.compile(PATTERN), Arrays.asList(new DatastoreClassLoaderDelegate(r)));
			
			Class c = loader.loadClass("honeycrm.server.test.small.DynamicallyLoadedClass", true);
			//assertTrue(c.newInstance() instanceof Plugin);
			// Plugin p = (Plugin) c.newInstance();
			// assertEquals("42", p.request());
			assertEquals("42", c.getMethod("request").invoke(c.newInstance()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testDynamicLoadingFromJarFileWithTwoClasses() {
		try {
			ResourceStore r = new ResourceStore(db, "Classes");
			InterceptClassLoader loader = new InterceptClassLoader(getClass().getClassLoader(), Pattern.compile(PATTERN), Arrays.asList(new DatastoreClassLoaderDelegate(r)));
			
			r.put(store.getBytecodeMapFromJarInputStream(new FileInputStream(FILE2)));
			
			Class c = loader.loadClass("honeycrm.server.test.small.DynamicallyLoadedClass");
			// assertTrue(c.getInterfaces()[0].equals(Plugin.class));
			// Plugin p = (Plugin) c.newInstance();
			// assertEquals("42", p.request());
			assertEquals("42", c.getMethod("request").invoke(c.newInstance()));
			
			Class c2 = loader.loadClass("honeycrm.server.test.small.DynamicallyLoadedClassTwo");
			// assertTrue(c2.getInterfaces()[0].equals(Plugin.class));
			// Plugin p2 = (Plugin) c2.newInstance();
			// assertEquals("23", p2.request());
			assertEquals("23", c2.getMethod("request").invoke(c2.newInstance()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
