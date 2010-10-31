package honeycrm.server.test.large;

import java.util.Random;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Timer;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Contact;
import honeycrm.server.services.ReportServiceImpl;
import honeycrm.server.test.medium.DatastoreTest;
import com.google.gwt.user.client.Command;

public class CrudOperationTests extends DatastoreTest {
	private ReportServiceImpl reportService = reportService = new ReportServiceImpl();

	public void testCreating() {
		final Dto dto = new Dto(Contact.class.getSimpleName());
		assertTrue(createService.create(dto) > 0);
	}

	public void testReading() {
		assertNull(readService.get(Contact.class.getSimpleName(), -1));
		assertNotNull(readService.getAll(Contact.class.getSimpleName(), 0, 20));
		try {
			readService.getAllRelated(0L, Contact.class.getSimpleName());
			fail();
		} catch (final RuntimeException e) {
		}
		assertNotNull(readService.getAllAssignedTo(Contact.class.getSimpleName(), 1, 0, 20));

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
					dto.setModule(Contact.class.getSimpleName());
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

					ids[i] = createService.create(dto);
				}
			}
		});
		System.out.println("Created " + count + " entities in " + createTime + " ms (" + (count / createTime) + " entities/ms)");

		final long getRandomTime = Timer.getTime(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < count; i++) {
					final long randomId = ids[r.nextInt((int) count)];
					readService.get(Contact.class.getSimpleName(), randomId);
				}
			}
		});
		System.out.println("Got " + count + " random entities in " + getRandomTime + " ms (" + (count / getRandomTime) + " entities/ms)");

		final float getAllIterations = 10;
		for (int i = 0; i < getAllIterations; i++) {
			final long getAllTime = Timer.getTime(new Command() {
				@Override
				public void execute() {
					readService.getAll("contact", 0, (int) count);
				}
			});
			System.out.println("getAll " + (1 + i) + "/" + getAllIterations + " times for " + count + " entities in " + getAllTime + "ms (" + (count / getAllTime) + " entities/ms)");
		}
	}

	public void testUpdating() {
		final Dto before = DemoDataProvider.contact();
		before.set("name", "before");

		final long id = createService.create(before);
		assertTrue(id > 0);

		final Dto loaded = readService.get(Contact.class.getSimpleName(), id);
		assertEquals("before", loaded.get("name"));
		loaded.set("name", "after");

		updateService.update(loaded);

		final Dto after = readService.get(Contact.class.getSimpleName(), id);
		assertEquals("after", after.get("name"));
	}

	public void testDeleting() {
		final long id = createService.create((DemoDataProvider.contact()));
		assertNotNull(readService.get(Contact.class.getSimpleName(), id));
		deleteService.delete(Contact.class.getSimpleName(), id);
		assertNull(readService.get(Contact.class.getSimpleName(), id));

		deleteService.deleteAll(Contact.class.getSimpleName());
	}
}
