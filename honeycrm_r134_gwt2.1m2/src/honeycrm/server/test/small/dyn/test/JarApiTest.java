package honeycrm.server.test.small.dyn.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class JarApiTest extends AbstractClassLoadingTest {
	public void testIfNamesAreCorrect() throws FileNotFoundException, IOException {
		for (final String name : getBytecodeMapFromJarInputStream(FILE2).keySet()) {
			assertTrue(name.endsWith(".class"));
			assertTrue(name.contains("/"));
		}
	}
	
	public void testGetClassBytecodeFromJarFileWithOneFile() throws FileNotFoundException, IOException {
		assertFalse(getBytecodeMapFromJarInputStream(FILE).isEmpty());
		assertEquals(1, getBytecodeMapFromJarInputStream(FILE).size());
	}

	public void testGetClassBytecodeFromJarFileWithTwoFiles() throws FileNotFoundException, IOException {
		assertFalse(getBytecodeMapFromJarInputStream(FILE2).isEmpty());
		assertEquals(2, getBytecodeMapFromJarInputStream(FILE2).size());
	}
	
	public void testIfBytecodeIsInstantiable() throws FileNotFoundException, IOException {
		final TestClassLoader loader = new TestClassLoader();
		
		for (final Map.Entry<String, byte[]> entry: getBytecodeMapFromJarInputStream(FILE2).entrySet()) {
			final String name = entry.getKey().replace("/", ".").replace(".class", "");
			loader.defineClass(name, entry.getValue());
		}
	}
	
	class TestClassLoader extends ClassLoader {
		public void defineClass(final String name, final byte[] bytes) {
			super.defineClass(name, bytes, 0, bytes.length);
		}
	}
}
