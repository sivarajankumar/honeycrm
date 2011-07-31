package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.services.CreateServiceImpl;
import honeycrm.server.services.ReadServiceImpl;

import java.util.ArrayList;

public class CreateServiceTest extends DatastoreTest {
	private final ReadServiceImpl reader = new ReadServiceImpl();
	private final CreateServiceImpl create = new CreateServiceImpl();

	private Dto getContact() {
		final Dto contact = new Dto("Contact");
		contact.set("name", "Foo");
		return contact;
	}

	public void testCreate() {
		final long id = create.create(getContact());
		assertTrue(id > 0);
	}

	public void testCreateAndCompare() {
		final Dto contact = getContact();
		final long id = create.create(contact);

		final Dto dto = reader.get("Contact", id);

		assertEquals(dto.getModule(), "Contact");
		assertEquals(contact.get("name"), dto.get("name"));
	}

	public void testCreateWithOneToManyRelationship() {
		final Dto product = new Dto("Product");
		product.set("name", "The Product");

		final long productId = create.create(product);

		final Dto service = new Dto("UniqueService");
		service.set("productId", productId);

		final ArrayList<Dto> services = new ArrayList<Dto>();
		services.add(service);

		final Dto contract = new Dto("Contract");
		contract.set("uniqueServices", services);

		final long contractId = create.create(contract);

		final Dto retrievedContract = reader.get("Contract", contractId);

		assertNotNull(retrievedContract.get("uniqueServices"));
		assertTrue(retrievedContract.get("uniqueServices") instanceof ArrayList<?>);
		assertEquals(services.size(), ((ArrayList<?>) retrievedContract.get("uniqueServices")).size());
	}
}
