package crm.server.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.server.ReflectionHelper;

public class IANATest extends TestCase {
	public void testMarshalling() {
		try {
			final Set<Integer> marshalIndices = new HashSet<Integer>();

			for (final Class<? extends AbstractDto> clazz : ReflectionHelper.getClassesWithPrefix("crm.client.dto", "Dto")) {
				try {
					final int newIndex = IANA.mashal(clazz);
					assertNotNull(newIndex);
					if (marshalIndices.contains(newIndex)) {
						// fails when running in code coverage mode (eclemma)
						System.err.println("already saw marshalling index " + newIndex + " before.");
						fail();
					}
					marshalIndices.add(newIndex);
				} catch (RuntimeException e) {
					fail(); // assume runtime exception caused by IANA.marshal method caused by invalid index.
				}
			}

			for (final int marshalledIndex : marshalIndices) {
				try {
					final Class<? extends AbstractDto> clazz = IANA.unmarshal(marshalledIndex);
					assertNotNull(clazz);
				} catch (RuntimeException e) {
					fail(); // assume runtime exception caused by invalid index that cannot be unmarshalled to class
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testForceException() {
		try {
			IANA.mashal(AbstractDto.class);
			fail(); // should never reach this point. expect exception to be thrown.
		} catch (RuntimeException e) {
		}

		try {
			IANA.mashal(null);
			fail(); // should never reach this point. expect exception to be thrown.
		} catch (RuntimeException e) {
		}
	}
}
