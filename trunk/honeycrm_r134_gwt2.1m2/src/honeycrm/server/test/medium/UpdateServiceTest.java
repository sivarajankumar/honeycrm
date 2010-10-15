package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.services.CreateServiceImpl;
import honeycrm.server.services.ReadServiceImpl;
import honeycrm.server.services.UpdateServiceImpl;

public class UpdateServiceTest extends DatastoreTest {
	private final CreateServiceImpl create = new CreateServiceImpl();
	private final UpdateServiceImpl updater = new UpdateServiceImpl();
	private final ReadServiceImpl reader = new ReadServiceImpl();

	public void testUpdating() {
		final Dto contact = new Dto("Contact");
		contact.set("name", "Foo");

		final long contactId = create.create(contact);

		final Dto retrievedContact1 = reader.get("Contact", contactId);
		assertEquals("Foo", retrievedContact1.get("name"));
		
		contact.setId(contactId);
		contact.set("name", "Bar");

		updater.update(contact);

		final Dto retrievedContact2 = reader.get("Contact", contactId);
		assertEquals("Bar", retrievedContact2.get("name"));
	}
	
	public void testUpdatingRelateField() {
		final Dto contact = new Dto("Contact");
		final long id = create.create(contact);
		
		final Dto retrievedContact = reader.get("Contact", id);
		retrievedContact.set("accountId", 0L); // <- this indicates that nothing has been selected yet. so no Key should be created either.
		updater.update(retrievedContact);
	
		final Dto retrievedContact2 = reader.get("Contact", id);
		assertEquals(retrievedContact.get("accountId"), retrievedContact2.get("accountId"));
	}

	public void testUpdatingRelateField2() {
		final Dto contact = new Dto("Contact");
		final long contactId = create.create(contact);
		
		final Dto offering = new Dto("Offering");
		offering.set("contactId", contactId);
		final long offeringId = create.create(offering);
		
		final Dto retrievedOffering = reader.get("Offering", offeringId);
		
		retrievedOffering.set("someupdatedfield", 42);
		updater.update(retrievedOffering);
		
		create.create(retrievedOffering);
	}
	
	public void testUpdatingWithOneToMany() {
		
	}
}
