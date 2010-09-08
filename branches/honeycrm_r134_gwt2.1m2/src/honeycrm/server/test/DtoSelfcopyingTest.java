package honeycrm.server.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.CollectionHelper;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

public class DtoSelfcopyingTest extends TestCase {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final CommonServiceImpl commonService = new CommonServiceImpl();
	private final DtoCopyMachine copy = new DtoCopyMachine();
	private final Random r = new Random();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}

	public void testCopy() {
		final Dto contact = copy.copy(DemoDataProvider.contact());
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
		dtoOffering.setModule("offering");
		dtoOffering.set("services_objects", CollectionHelper.toList(s1/* , s2 */));

		final Dto offeringCopy = dtoOffering.copy();
		final Dto copyService = (Dto) ((List<?>) offeringCopy.get("services_objects")).get(0);

		for (final String field : copyService.getAllData().keySet()) {
			assertEquals(s1.get(field), copyService.getAllData().get(field));
		}

		final long idOffering = commonService.create(dtoOffering);
		final Dto retrievedOffering = commonService.get("offering", idOffering);
		assertNotNull(retrievedOffering.get("services_objects"));
		
		
		offeringCopy.setModule("contract");
		final long idContract = commonService.create(offeringCopy);

		final Dto retrievedContract = commonService.get("contract", idContract);
		final Dto contractService = (Dto) ((List<?>) retrievedContract.get("services_objects")).get(0);
		for (final String field : contractService.getAllData().keySet()) {
			if (null == s1.get(field)) {
				System.out.println(field);
				continue;
			}
			assertEquals(s1.get(field), contractService.get(field));
		}
	}

	private LinkedList<Dto> getServices() {
		final Dto s1 = getService("%");
		final Dto s2 = getService("abs");

		final LinkedList<Dto> services = new LinkedList<Dto>();
		services.add(s1);
		services.add(s2);

		return services;
	}

	private Dto getService(final String kindOfDiscount) {
		final Dto service = new Dto();
		service.setModule("service");
		service.set("price", r.nextDouble());
		service.set("quantity", r.nextInt());
		service.set("discount", r.nextInt());
		service.set("kindOfDiscount", kindOfDiscount);
		return service;
	}
}
