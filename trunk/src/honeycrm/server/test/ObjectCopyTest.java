package honeycrm.server.test;

import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.server.CopyMachine;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;

import java.util.Random;

import junit.framework.TestCase;

public class ObjectCopyTest extends TestCase {
	private static final int ITERATIONS = 10000;
	private CopyMachine m;

	@Override
	protected void setUp() throws Exception {
		m = new CopyMachine();
	}

	// TODO test for copying between incompatible objects
	// TODO iterate over all members automatically and check for equality
	public void testCopy() {
		try {
			DtoContact dto = new DtoContact();
			dto.setId(new Random().nextLong());
			dto.setName(String.valueOf(new Random().nextLong()));
			dto.setAccountID(new Random().nextLong());

			// DTO -> Domain class
			Contact domain = (Contact) m.copy(dto, Contact.class);
			assertNotNull(domain);
			assertEquals(dto.getName(), domain.getName());
			// cannot set the id field in this class because instantiation of keys is not supported
			// yet.
			// assertEquals(dto.getId(), domain.getId());
			assertEquals(dto.getAccountID(), domain.getAccountID());

			// Domain class -> DTO class
			DtoContact dtoCopy = (DtoContact) m.copy(domain, DtoContact.class);
			assertNotNull(dtoCopy);
			assertEquals(dtoCopy.getName(), domain.getName());
			assertEquals(dtoCopy.getAccountID(), domain.getAccountID());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testCopyTimed() {
		final long before = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			Account a = DemoDataProvider.account();
			DtoAccount dto = (DtoAccount) m.copy(a, DtoAccount.class);

			assertEquals(a.getName(), dto.getName());
			assertEquals(a.getAddress(), dto.getAddress());
		}

		System.out.println("Copying took " + (System.currentTimeMillis() - before) + " ms.");
	}

	public void testGetUpdatedInstance() {
		final long before = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {
			// represents the current object state loaded from the db
			final DtoContact a = (DtoContact) m.copy(DemoDataProvider.contact(), DtoContact.class);
			// represents the state updated by the user in the gui, i.e. is a DTO object coming from
			// UI code.
			final DtoContact b = (DtoContact) m.copy(a, DtoContact.class);
			// user has changed the name field like this
			b.setName(b.getName() + "THE CHANGE");

			// copy all the changes into the old db object (a) and return c containing the changes
			m.getUpdatedInstance(b, a);
		}

		System.out.println("getUpdatedInstance copying took " + (System.currentTimeMillis() - before) + " ms.");
	}
}
