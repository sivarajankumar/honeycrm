package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

public class DtoSelfcopyingTest extends TestCase {
	private final DtoCopyMachine copy = new DtoCopyMachine();

	public void testCopy() {
		final Dto contact = copy.copy(DemoDataProvider.contact());
		final Dto contactCopy = contact.copy();

		for (final String key : contact.getAllData().keySet()) {
			if (key.equals("id")) {
				continue;
			}
			assertEquals(contact.get(key), contactCopy.get(key));
		}
	}
}
