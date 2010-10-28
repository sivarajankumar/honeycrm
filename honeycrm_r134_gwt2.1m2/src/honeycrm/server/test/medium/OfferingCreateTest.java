package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Offering;
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

			final long id = createService.create(offering);

			final Dto o = readService.get(Offering.class.getSimpleName(), id);
			assertNotNull(o.get("deadline"));
			assertNotNull(o.get("uniqueServices"));
			assertEquals(productIds.size(), ((Collection<Dto>) o.get("uniqueServices")).size());
		}
	}

	private Dto getOffering(final ArrayList<Dto> services) {
		final Dto offering = new Dto();
		offering.setModule(Offering.class.getSimpleName());
		offering.set("deadline", new Date(System.currentTimeMillis()));
		offering.set("uniqueServices", services);
		return offering;
	}

	private ArrayList<Dto> getServices(final Set<Long> productIds) {
		final ArrayList<Dto> services = new ArrayList<Dto>();

		for (final Long productId : productIds) {
			final Dto s = new Dto();
			s.setModule(UniqueService.class.getSimpleName());
			s.set("productID", productId);
			services.add(s);
		}

		return services;
	}

	private Set<Long> createProducts() {
		final Set<Long> ids = new HashSet<Long>();

		for (final Dto product: DemoDataProvider.getProducts(2)) {
			ids.add(createService.create(product));
		}

		return ids;
	}
}
