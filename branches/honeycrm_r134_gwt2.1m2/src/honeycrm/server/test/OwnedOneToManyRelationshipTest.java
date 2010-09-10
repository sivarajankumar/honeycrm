package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;
import honeycrm.client.dto.Dto;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.UniqueService;

public class OwnedOneToManyRelationshipTest extends DatastoreTest {
	public void testCreate() {
		for (int i = 0; i < 50; i++) {
			final int childCountFoo = 2; // r.nextInt(20);
			final int childCountBar = 1; // r.nextInt(20);
			final int childCountBaz = 0; // r.nextInt(20);

			final Dto foo = getOffering(1, childCountFoo);
			final Dto bar = getOffering(2, childCountBar);
			final Dto baz = getOffering(3, childCountBaz);

			final long idFoo = commonService.create(foo);
			final long idBar = commonService.create(bar);
			final long idBaz = commonService.create(baz);

			final Dto dtoFoo = commonService.get("offering", idFoo);
			final Dto dtoBar = commonService.get("offering", idBar);
			final Dto dtoBaz = commonService.get("offering", idBaz);

			assertEquals(childCountFoo, ((List<?>) dtoFoo.get("services")).size());
			assertEquals(childCountBar, ((List<?>) dtoBar.get("services")).size());
			//assertEquals(childCountBaz, ((List<?>) dtoBaz.get("services_objects")).size());
		}
	}

	private Dto getService(final int name) {
		final Dto service = new Dto();
		service.setModule(UniqueService.class.getSimpleName().toLowerCase());
		service.set("name", "child " + name);
		return service;
	}

	private Dto getOffering(final int name, final int childCount) {
		final Dto offering = new Dto();
		offering.setModule(Offering.class.getSimpleName().toLowerCase());
		offering.set("name", "offering " + name);
		offering.set("services", getServices(childCount));
		return offering;
	}

	private ArrayList<Dto> getServices(final int childCount) {
		final ArrayList<Dto> services = new ArrayList<Dto>(childCount);
		for (int i = 0; i < childCount; i++) {
			services.add(getService(random.nextInt()));
		}
		return services;
	}
}
