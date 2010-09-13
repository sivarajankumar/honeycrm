package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.UniqueService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class OfferingCreateTest extends DatastoreTest {
	public void testCreateOfferings() {
		final Set<Long> productIds = createProducts();
		
		for (int i = 0; i < 10; i++) {
			final ArrayList<Dto> services = getServices(productIds);
			final Dto offering = getOffering(services);

			final long id = commonService.create(offering);

			final Dto o = commonService.get("offering", id);
			assertNotNull(o.get("deadline"));
			assertNotNull(o.get("services"));
			assertEquals(productIds.size(), ((Collection<Dto>) o.get("services")).size());
		}
	}

	private Dto getOffering(final ArrayList<Dto> services) {
		final Dto offering = new Dto();
		offering.setModule("offering");
		offering.set("deadline", new Date(System.currentTimeMillis()));
		offering.set("services", services);
		return offering;
	}

	private ArrayList<Dto> getServices(final Set<Long> productIds) {
		final ArrayList<Dto> services = new ArrayList<Dto>();

		for (final Long productId : productIds) {
			final Dto s = new Dto();
			s.setModule(UniqueService.class.getSimpleName().toLowerCase());
			s.set("productID", productId);
			services.add(s);
		}

		return services;
	}

	private Set<Long> createProducts() {
		final Set<Long> ids = new HashSet<Long>();

		for (final Dto product: DemoDataProvider.getProducts(2)) {
			ids.add(commonService.create(product));
		}

		return ids;
	}
}
