package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.PMF;
import honeycrm.server.domain.DiscountableService;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.RecurringService;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.transfer.DtoCopyMachine;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

public class OneToManyTest extends DatastoreTest {
	private final PersistenceManager m = PMF.get().getPersistenceManager();
	private final DtoCopyMachine copy = new DtoCopyMachine();

	public void testCopyDtoWithOneToMany() {
		final Dto dto = new Dto();
		dto.setModule(Offering.class.getSimpleName().toLowerCase());

		final ArrayList<Dto> uniqueServices = new ArrayList<Dto>();
		uniqueServices.add(getDtoService(UniqueService.class.getSimpleName().toLowerCase()));

		final ArrayList<Dto> recurringServices = new ArrayList<Dto>();
		recurringServices.add(getDtoService(RecurringService.class.getSimpleName().toLowerCase()));
		
		dto.set("services", uniqueServices);
		dto.set("recurringServices", recurringServices);

		final Offering offering = (Offering) copy.copy(dto);

		assertEquals(1, offering.services.size());
		assertEquals(1, offering.recurringServices.size());
	}

	public void testCopyDomainObjectWithOneToMany() {
		final int serviceCount = 1;
		final Offering offering = new Offering();
		offering.services = createAndPersistServices(serviceCount, false);
		offering.recurringServices = createAndPersistServices(serviceCount, true);
		
		final Dto dto = copy.copy(offering, true);

		assertNotNull(dto.get("services"));
		assertEquals(serviceCount, ((List<?>) dto.get("services")).size());
		assertEquals(serviceCount, ((List<?>) dto.get("recurringServices")).size());
		
		final long id = commonService.create(dto);
		
		final Dto retrievedDto = commonService.get("offering", id);
		assertEquals(serviceCount, ((List<?>) retrievedDto.get("services")).size());
		assertEquals(serviceCount, ((List<?>) retrievedDto.get("recurringServices")).size());
	}

	private ArrayList<Key> createAndPersistServices(final int serviceCount, boolean uniqueServices) {
		final ArrayList<Key> keys = new ArrayList<Key>();

		for (int i = 0; i < serviceCount; i++) {
			final DiscountableService s = getService(uniqueServices);
			m.makePersistent(s);
			keys.add(s.id);
		}

		return keys;
	}

	private DiscountableService getService(boolean uniqueServices) {
		final DiscountableService service = uniqueServices ? new UniqueService() : new RecurringService();
		service.name = "service" + random.nextInt();
		service.price = random.nextDouble();
		return service;
	}

	private Dto getDtoService(final String moduleName) {
		final Dto dto = new Dto();
		dto.setModule(moduleName);
		dto.set("name", "service " + random.nextInt());
		return dto;
	}
}
