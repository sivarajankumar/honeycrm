package honeycrm.server.test;

import honeycrm.client.dto.Dto;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class CrudOperationTests extends TestCase {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private CommonServiceImpl commonService;
	private final DtoCopyMachine copy = new DtoCopyMachine();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
		commonService = new CommonServiceImpl();
	}

	@Override
	protected void tearDown() throws Exception {
		helper.tearDown();
	}

	public void testCreating() {
		final Dto dto = copy.copy(DemoDataProvider.contact());
		assertTrue(commonService.create(dto) > 0);
	}
	
	public void testReading() {
		assertNull(commonService.get("contact", -1));
		assertNotNull(commonService.getAll("contact", 0, 20));
		assertNotNull(commonService.getAllRelated(0L, "contact"));
		assertNotNull(commonService.getAllAssignedTo("contact", 1, 0, 20));
		
		assertNotNull(commonService.getAnnuallyOfferingVolumes());
	}
	
	public void testUpdating() {
		final Dto before = copy.copy(DemoDataProvider.contact());
		before.set("name", "before");

		final long id = commonService.create(before);
		assertTrue(id > 0);
		
		final Dto loaded = commonService.get("contact", id);
		assertEquals("before", loaded.get("name"));
		loaded.set("name", "after");
		
		commonService.update(loaded, id);
		
		final Dto after = commonService.get("contact", id);
		assertEquals("after", after.get("name"));
	}
	
	public void testDeleting() {
		final long id = commonService.create(copy.copy(DemoDataProvider.contact()));
		assertNotNull(commonService.get("contact", id));
		commonService.delete("contact", id);
		assertNull(commonService.get("contact", id));
		
		commonService.deleteAll("contact");
	}
}
