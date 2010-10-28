package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Contract;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.transfer.DtoCopyMachine;

import java.util.LinkedList;
import java.util.List;

public class DtoSelfcopyingTest extends DatastoreTest {
	private final DtoCopyMachine copy = new DtoCopyMachine();

	public void testCopy() {
		final Dto contact = DemoDataProvider.contact();
		final Dto contactCopy = contact.copy();

		for (final String key : contact.getAllData().keySet()) {
			if (key.equals("id")) {
				continue;
			}
			assertEquals(contact.get(key), contactCopy.get(key));
		}
	}

	public void testCopyOffering() {
		final Dto s1 = getService("%");
		final Dto s2 = getService("abs");

		final LinkedList<Dto> services = new LinkedList<Dto>();
		services.add(s1);
		services.add(s2);

		final Dto dtoOffering = new Dto();
		dtoOffering.setModule(Offering.class.getSimpleName());
		dtoOffering.set("uniqueServices", CollectionHelper.toList(s1/* , s2 */));

		final Dto offeringCopy = dtoOffering.copy();
		assertNotNull(offeringCopy.get("uniqueServices"));

		/*for (final String field : copyService.getAllData().keySet()) {
			assertEquals(s1.get(field), copyService.getAllData().get(field));
		}*/

		final long idOffering = createService.create(dtoOffering);
		final Dto retrievedOffering = readService.get(Offering.class.getSimpleName(), idOffering);
		assertNotNull(retrievedOffering.get("uniqueServices"));
		
		
		offeringCopy.setModule(Contract.class.getSimpleName());
		final long idContract = createService.create(offeringCopy);

		final Dto retrievedContract = readService.get(Contract.class.getSimpleName(), idContract);
		final Dto contractService = (Dto) ((List<?>) retrievedContract.get("uniqueServices")).get(0);
		/*for (final String field : contractService.getAllData().keySet()) {
			if (null == s1.get(field)) {
				System.out.println(field);
				continue;
			}
			assertEquals(s1.get(field), contractService.get(field));
		}*/
	}

	private Dto getService(final String kindOfDiscount) {
		final Dto service = new Dto();
		service.setModule(UniqueService.class.getSimpleName());
		service.set("price", random.nextDouble());
		service.set("quantity", random.nextInt());
		service.set("discount", random.nextInt());
		service.set("kindOfDiscount", kindOfDiscount);
		return service;
	}
}
