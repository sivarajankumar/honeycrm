package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.CommonServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class OfferingCreateTest extends TestCase {
	private final Random r = new Random();
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private CommonServiceImpl commonService;
	private Set<Long> productIds;

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
		commonService = new CommonServiceImpl();
		productIds = createProducts();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}

	public void testCreate() {
		final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		for (int i = 0; i < 100; i++) {
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
			s.setModule("service");
			s.set("productID", productId);
			services.add(s);
		}

		return services;
	}

	private Set<Long> createProducts() {
		final Set<Long> ids = new HashSet<Long>();
		final Dto product = new Dto();
		product.setModule("product");

		for (int i = 0; i < 2; i++) {
			product.set("name", "product " + r.nextLong());
			product.set("price", r.nextDouble());

			ids.add(commonService.create(product));
		}

		return ids;
	}
}
