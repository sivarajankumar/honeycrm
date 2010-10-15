package honeycrm.server.test.large;

import java.util.Random;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Timer;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.services.ReportServiceImpl;
import honeycrm.server.transfer.DtoCopyMachine;
import junit.framework.TestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gwt.user.client.Command;

public class CrudOperationTests extends TestCase {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private CommonServiceImpl commonService;
	private ReportServiceImpl reportService;
	private final DtoCopyMachine copy = new DtoCopyMachine();

	@Override
	protected void setUp() throws Exception {
		helper.setUp();
		commonService = new CommonServiceImpl();
		reportService = new ReportServiceImpl();
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

		assertNotNull(reportService.getAnnuallyOfferingVolumes());
	}

	public void testReadPerformance() {
		final Random r = new Random();
		final float count = 1000;

		final long ids[] = new long[(int) count];

		final long createTime = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					final Dto dto = new Dto();
					dto.setModule("contact");
					final String suffix = String.valueOf(r.nextLong());
					dto.set("name", "some contact" + suffix);
					dto.set("email", "email" + suffix);
					dto.set("name", "name" + suffix);
					dto.set("city", "city" + suffix);
					dto.set("phone", "phone" + suffix);
					dto.set("emailOptedOut", r.nextBoolean());
					dto.set("doNotCall", r.nextBoolean());
					dto.set("mobile", "mobile" + suffix);
					dto.set("doNotCallExplanation", "explanation" + suffix);
					dto.set("bankAccountData", "bank" + suffix);
					dto.set("profession", "Student" + suffix);
					dto.set("study", "Biology" + suffix);

					ids[i] = commonService.create(dto);
				}
			}
		});
		System.out.println("Created " + count + " entities in " + createTime + " ms (" + (count / createTime) + " entities/ms)");

		final long getRandomTime = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					final long randomId = ids[r.nextInt((int) count)];
					commonService.get("contact", randomId);
				}
			}
		});
		System.out.println("Got " + count + " random entities in " + getRandomTime + " ms (" + (count / getRandomTime) + " entities/ms)");

		final float getAllIterations = 10;
		for (int i = 0; i < getAllIterations; i++) {
			final long getAllTime = Timer.getTime(new Command() {
				@Override
				public void execute() {
					commonService.getAll("contact", 0, (int) count);
				}
			});
			System.out.println("getAll " + (1 + i) + "/" + getAllIterations + " times for " + count + " entities in " + getAllTime + "ms (" + (count / getAllTime) + " entities/ms)");
		}
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
