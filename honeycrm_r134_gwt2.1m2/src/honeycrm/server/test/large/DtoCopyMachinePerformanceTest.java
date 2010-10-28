package honeycrm.server.test.large;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

public class DtoCopyMachinePerformanceTest extends TestCase {
	private static final int ENTITY_COUNT = 50000;

	public void testCopying() {
		final DtoCopyMachine copy = new DtoCopyMachine();
		System.out.print("Get demo accounts..");
		final Dto[] accounts = getDemoAccounts();
		final Dto[] dtos = new Dto[ENTITY_COUNT];

		final long before = System.currentTimeMillis();
		System.out.print("Done.\nCopying..");
		for (int i = 0; i < ENTITY_COUNT; i++) {
		// TODO we have to measure the performance of the NewService.CopyMachine class instead.
		//	dtos[i] = copy.copy(accounts[i]);
		}
		final long diff = System.currentTimeMillis() - before;

		final double perEntity = (double) diff / (double) ENTITY_COUNT;
		final String timePerEntity = String.format("%.3f", 100.0 * perEntity);
		System.out.println("Done.\nDuration: " + diff + " ms, " + timePerEntity + " ms for 100 entities");
	}

	private Dto[] getDemoAccounts() {
		final Dto[] accounts = new Dto[ENTITY_COUNT];
		for (int i = 0; i < ENTITY_COUNT; i++) {
			accounts[i] = DemoDataProvider.account();
		}
		return accounts;
	}
}
