package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.RecurringService;
import honeycrm.server.domain.UniqueService;
import java.util.ArrayList;
import java.util.List;

public class OneToManyTest extends DatastoreTest {
	public void testCopyDtoWithOneToMany() {
		final Dto dto = new Dto();
		dto.setModule(Offering.class.getSimpleName());

		final ArrayList<Dto> uniqueServices = new ArrayList<Dto>();
		uniqueServices.add(getDtoService(UniqueService.class.getSimpleName()));

		final ArrayList<Dto> recurringServices = new ArrayList<Dto>();
		recurringServices.add(getDtoService(RecurringService.class.getSimpleName()));

		dto.set("uniqueServices", uniqueServices);
		dto.set("recurringServices", recurringServices);

		final long id = createService.create(dto);
		final Dto offering = readService.get(Offering.class.getSimpleName(), id);

		assertEquals(1, ((List<?>) offering.get("uniqueServices")).size());
		assertEquals(1, ((List<?>) offering.get("recurringServices")).size());
	}

	public void testCopyDomainObjectWithOneToMany() {
		final int serviceCount = 1;
		final Dto offering = new Dto(Offering.class.getSimpleName());
		offering.set("uniqueServices", createAndPersistServices(serviceCount, false));
		offering.set("recurringServices", createAndPersistServices(serviceCount, true));

		final long id = createService.create(offering);
		final Dto dto = readService.get(Offering.class.getSimpleName(), id);
		
		assertNotNull(dto.get("uniqueServices"));
		assertEquals(serviceCount, ((List<?>) dto.get("uniqueServices")).size());
		assertEquals(serviceCount, ((List<?>) dto.get("recurringServices")).size());
	}

	private ArrayList<Dto> createAndPersistServices(final int serviceCount, boolean uniqueServices) {
		final ArrayList<Dto> dtos = new ArrayList<Dto>();

		for (int i = 0; i < serviceCount; i++) {
			final Dto s = getService(uniqueServices);
			final long id = createService.create(s);
			dtos.add(readService.get(s.getModule(), id));
		}

		return dtos;
	}

	private Dto getService(boolean uniqueServices) {
		final Dto service = new Dto((uniqueServices ? new UniqueService() : new RecurringService()).getClass().getSimpleName());
		service.set("name", "service" + random.nextInt());
		service.set("price", random.nextDouble());
		return service;
	}

	private Dto getDtoService(final String moduleName) {
		final Dto dto = new Dto();
		dto.setModule(moduleName);
		dto.set("name", "service " + random.nextInt());
		return dto;
	}
}
