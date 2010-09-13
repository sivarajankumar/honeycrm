package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Contract;

public class ContractRetrievePerformanceTest extends DatastoreTest {
	public void testCreateAndRead() throws InterruptedException {
		final int count = 10;

		final List<Long> productIds = new ArrayList<Long>();

		for (final Dto product : DemoDataProvider.getProducts(100)) {
			productIds.add(commonService.create(product));
		}

		final ArrayList<Dto> services = DemoDataProvider.getServices(20);

		for (final Dto service : services) {
			service.set("productID", productIds.get(random.nextInt(count)));
		}

		final Dto contract = new Dto();
		contract.setModule("contract");
		contract.set("services", services);

		final long id = commonService.create(contract);

		for (int i = 0; i < 1000; i++) {
			final Dto retrieved = commonService.get(Contract.class.getSimpleName().toLowerCase(), id);
			assertNotNull(retrieved);
		}

		// for profiling this might be useful..
		// Thread.sleep(1000 * 60 * 60);
	}
}
