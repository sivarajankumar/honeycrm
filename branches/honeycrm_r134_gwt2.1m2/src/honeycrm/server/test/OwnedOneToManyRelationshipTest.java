package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import honeycrm.client.dto.Dto;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.transfer.DtoCopyMachine;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import junit.framework.TestCase;

public class OwnedOneToManyRelationshipTest extends TestCase {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final CommonServiceImpl commonService = new CommonServiceImpl();
	private final Random r = new Random(System.currentTimeMillis());
	private final DtoCopyMachine copy = new DtoCopyMachine();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}

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

			assertEquals(childCountFoo, ((List<?>) dtoFoo.get("services_objects")).size());
			assertEquals(childCountBar, ((List<?>) dtoBar.get("services_objects")).size());
			//assertEquals(childCountBaz, ((List<?>) dtoBaz.get("services_objects")).size());
		}
	}

	private Dto getService(final int name) {
		final Dto service = new Dto();
		service.setModule("service");
		service.set("name", "child " + name);
		return service;
	}

	private Dto getOffering(final int name, final int childCount) {
		final Dto offering = new Dto();
		offering.setModule("offering");
		offering.set("name", "offering " + name);
		offering.set("services_objects", getServices(childCount));
		return offering;
	}

	private ArrayList<Dto> getServices(final int childCount) {
		final ArrayList<Dto> services = new ArrayList<Dto>(childCount);
		for (int i = 0; i < childCount; i++) {
			services.add(getService(r.nextInt()));
		}
		return services;
	}
}
