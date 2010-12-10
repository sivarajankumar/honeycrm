package honeycrm.server.test.small.dyn.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import honeycrm.server.test.medium.DatastoreTest;

abstract public class AbstractClassLoadingTest extends DatastoreTest {
	protected static final File FILE = new File("dynamicLoadingTesting.jar");
	protected static final File FILE2 = new File("dynamicLoadingTestingTwoFiles.jar");
	protected DatastoreService db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = DatastoreServiceFactory.getDatastoreService();
	}

	protected byte[] getBytesOfFile(final File file) throws FileNotFoundException, IOException {
		byte[] bytes = new byte[8096];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		while (in.read(bytes) > 0) {
			out.write(bytes);
		}

		return out.toByteArray();
	}

	protected HashMap<String, byte[]> getBytecodeMapFromJarInputStream(final File file) throws IOException, FileNotFoundException {
		final HashMap<String, byte[]> map = new HashMap<String, byte[]>();
		final JarInputStream in = new JarInputStream(new FileInputStream(file));
		
		JarEntry e;
		while (null != (e = in.getNextJarEntry())) {
			System.out.println("at entry: " + e.getName());

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			int bytecode;
			
			while ((bytecode = in.read()) != -1) {
				out.write(bytecode);
			}

			map.put(e.getName(), out.toByteArray());
		}
		return map;
	}
}
