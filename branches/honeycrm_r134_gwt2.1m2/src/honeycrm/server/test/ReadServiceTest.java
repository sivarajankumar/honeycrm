package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.Timer;
import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;
import honeycrm.server.domain.Contract;
import honeycrm.server.domain.Product;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.services.ReadServiceImpl;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.Command;

public class ReadServiceTest extends DatastoreTest {
	private static final ReadServiceImpl service = new ReadServiceImpl();
	private static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();

	public void testGet() {
		final Entity e = createContact();
		final Dto dto = service.get(Contact.class.getSimpleName(), e.getKey().getId());

		assertEquals(e.getProperty("name"), dto.get("name"));
	}

	public void testGetAll() {
		for (int i = 0; i < 100; i++) {
			createContact();
		}

		final ListQueryResult r = service.getAll(Contact.class.getSimpleName());
		assertEquals(100, r.getItemCount());
		assertEquals(100, r.getResults().length);
	}

	public void testGetByName() {
		final Entity e = createContact();

		final Dto dto = service.getByName(Contact.class.getSimpleName(), e.getProperty("name").toString().substring(0, 3));
		assertEquals(e.getProperty("name").toString(), dto.get("name").toString());

		assertNull(service.getByName(Contact.class.getSimpleName(), String.valueOf(random.nextDouble())));
		assertNull(service.getByName(Contact.class.getSimpleName(), null));
	}

	public void testGetWithReferences() {
		final Entity account = createAccount();
		final Entity contact = createContact();
		contact.setProperty("accountID", account.getKey().getId());
		db.put(contact);

		final Dto cwa = service.get(Contact.class.getSimpleName(), contact.getKey().getId());
		assertTrue(null != cwa.get("accountID_resolved") && ((Dto) cwa.get("accountID_resolved")).get("name").equals(account.getProperty("name")));
	}

	public void testGetWithLists() {
		final Entity product = new Entity(Product.class.getSimpleName());
		product.setProperty("name", "product" + random.nextLong());
		db.put(product);

		final Entity uniqueService = new Entity(UniqueService.class.getSimpleName());
		uniqueService.setProperty("productID", product.getKey().getId());
		db.put(uniqueService);

		final ArrayList<Key> serviceKeys = new ArrayList<Key>(1);
		serviceKeys.add(uniqueService.getKey());

		final Entity contract = new Entity(Contract.class.getSimpleName());
		contract.setProperty("services", serviceKeys);
		db.put(contract);

		final Dto contractDto = service.get(Contract.class.getSimpleName(), contract.getKey().getId());
		assertEquals(1, ((List<?>) contractDto.get("services")).size());
		assertTrue(((List<?>) contractDto.get("services")).get(0) instanceof Dto);
		assertEquals(product.getKey().getId(), ((List<Dto>) contractDto.get("services")).get(0).get("productID"));
	}

	public void testGetPerformance() throws InterruptedException {
		final float count = 1000;

		final long[] keys = new long[(int) count];

		final long createTime = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					keys[i] = createContact().getKey().getId();
				}
			}
		});
		System.out.println("Created " + count + " entities in " + createTime + "ms (" + (count / createTime) + " entities/ms)");

		final long randomGetTime = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					final long randomId = keys[random.nextInt((int) count)];
					// db.get(KeyFactory.createKey("Contact", randomId));
					service.get("Contact", randomId);
				}
			}
		});
		System.out.println("Got " + count + " random entities in " + randomGetTime + "ms (" + (count / randomGetTime) + " entities/ms)");

		final float getAllIterations = 10;
		for (int i = 0; i < getAllIterations; i++) {
			final long getAllTime = Timer.getTime(new Command() {
				@Override
				public void execute() {
					service.getAll("Contact");
				}
			});
			System.out.println("getAll " + (1 + i) + "/" + getAllIterations + " times for " + count + " entities in " + getAllTime + "ms (" + (count / getAllTime) + " entities/ms)");
		}
		// Thread.sleep(1000 * 60 * 60);
	}

	private Entity createContact() {
		final String suffix = String.valueOf(random.nextLong());
		final Entity e = new Entity(Contact.class.getSimpleName());
		e.setProperty("email", "email" + suffix);
		e.setProperty("name", "name" + suffix);
		e.setProperty("city", "city" + suffix);
		e.setProperty("phone", "phone" + suffix);
		e.setProperty("emailOptedOut", random.nextBoolean());
		e.setProperty("doNotCall", random.nextBoolean());
		e.setProperty("mobile", "mobile" + suffix);
		e.setProperty("doNotCallExplanation", "explanation" + suffix);
		e.setProperty("bankAccountData", "bank" + suffix);
		e.setProperty("profession", "Student" + suffix);
		e.setProperty("study", "Biology" + suffix);
		db.put(e);
		return e;
	}

	private Entity createAccount() {
		final Entity e = new Entity(Account.class.getSimpleName());
		e.setProperty("name", "some account" + random.nextLong());
		db.put(e);
		return e;
	}

}
