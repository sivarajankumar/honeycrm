package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.services.CreateServiceImpl;
import honeycrm.server.services.ReadServiceImpl;

public class NewServiceTest extends DatastoreTest {
	private final CreateServiceImpl creator = new CreateServiceImpl();
	private final ReadServiceImpl reader = new ReadServiceImpl();

	public void testResolving() {
		final Dto account = new Dto();
		account.setModule("Account");
		account.set("name", "The Account");

		final long accountId = creator.create(account);

		final Dto contact = new Dto();
		contact.setModule("Contact");
		contact.set("name", "The Contact");
		contact.set("accountId", accountId);

		final long contactId = creator.create(contact);

		final Dto retrievedContact = reader.get("Contact", contactId);

		assertEquals(contact.get("name"), retrievedContact.get("name"));
		assertEquals(contact.get("accountId"), retrievedContact.get("accountId"));
		assertNotNull(retrievedContact.get("accountId_resolved"));
		assertTrue(retrievedContact.getId() == contactId);
		assertEquals(account.get("name"), ((Dto) retrievedContact.get("accountId_resolved")).get("name"));
		assertTrue(retrievedContact.get("accountId").equals(accountId));
	}
}
