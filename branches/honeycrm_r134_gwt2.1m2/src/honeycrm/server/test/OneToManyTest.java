package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

import honeycrm.client.dto.Dto;
import honeycrm.server.PMF;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.transfer.DtoCopyMachine;

public class OneToManyTest extends DatastoreTest {
	private final PersistenceManager m = PMF.get().getPersistenceManager();
	private final DtoCopyMachine copy = new DtoCopyMachine();

	public void testCopyDtoWithOneToMany() {
		final Dto dto = new Dto();
		dto.setModule(Offering.class.getSimpleName().toLowerCase());

		final ArrayList<Dto> services = new ArrayList<Dto>();
		services.add(getDtoService());

		dto.set("services", services);

		final Offering offering = (Offering) copy.copy(dto);

		assertEquals(1, offering.services.size());
	}

	public void testCopyDomainObjectWithOneToMany() {
		final int serviceCount = 1;
		final Offering offering = new Offering();
		offering.services = createAndPersistServices(serviceCount);

		final Dto dto = copy.copy(offering);

		assertNotNull(dto.get("services"));
		assertEquals(serviceCount, ((List<?>) dto.get("services")).size());
	}

	private ArrayList<Key> createAndPersistServices(final int serviceCount) {
		final ArrayList<Key> keys = new ArrayList<Key>();

		for (int i = 0; i < serviceCount; i++) {
			final UniqueService s = getService();
			m.makePersistent(s);
			keys.add(s.id);
		}

		return keys;
	}

	private UniqueService getService() {
		final UniqueService service = new UniqueService();
		service.name = "service" + random.nextInt();
		service.price = random.nextDouble();
		return service;
	}

	private Dto getDtoService() {
		final Dto dto = new Dto();
		dto.setModule(UniqueService.class.getSimpleName().toLowerCase());
		dto.set("name", "service " + random.nextInt());
		return dto;
	}
}
