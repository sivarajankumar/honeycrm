package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Contract;

import java.util.ArrayList;
import java.util.List;

public class ContractRetrievePerformanceTest extends DatastoreTest {
	public void testCreateAndRead() throws InterruptedException {
		final int count = 10;
		
		final String contractModule = Contract.class.getSimpleName().toLowerCase();

		final List<Long> productIds = new ArrayList<Long>();

		for (final Dto product : DemoDataProvider.getProducts(100)) {
			productIds.add(commonService.create(product));
		}

		final Dto contract = getContract(count, productIds);

		long lastId = -1;

		System.out.print("creating.. ");
		for (int i=0; i<100; i++) {
			lastId = commonService.create(contract);
		}
		System.out.println("done.");

		System.out.print("get all.. ");
		for (int i=0; i<10; i++) {
			commonService.getAll(contractModule, 0, 100);
		}
		System.out.println("done.");
		
		System.out.print("get.. ");
		for (int i = 0; i < 1000; i++) {
			final Dto retrieved = commonService.get(contractModule, lastId);
			assertNotNull(retrieved);
		}
		System.out.println("done.");

		// for profiling this might be useful..
		// Thread.sleep(1000 * 60 * 60);
	}

	private Dto getContract(final int count, final List<Long> productIds) {
		final ArrayList<Dto> services = DemoDataProvider.getServices(20);

		for (final Dto service : services) {
			service.set("productID", productIds.get(random.nextInt(count)));
		}

		final Dto contract = new Dto();
		contract.setModule("contract");
		contract.set("services", services);
		return contract;
	}
}
