package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Offering;

import java.util.List;

public class OwnedOneToManyRelationshipTest extends DatastoreTest {
	public void testCreate() {
		for (int i = 0; i < 50; i++) {
			final int childCountFoo = 2; // r.nextInt(20);
			final int childCountBar = 1; // r.nextInt(20);
			final int childCountBaz = 0; // r.nextInt(20);

			final Dto foo = DemoDataProvider.getOffering(1, childCountFoo);
			final Dto bar = DemoDataProvider.getOffering(2, childCountBar);
			final Dto baz = DemoDataProvider.getOffering(3, childCountBaz);

			final long idFoo = createService.create(foo);
			final long idBar = createService.create(bar);
			final long idBaz = createService.create(baz);

			final Dto dtoFoo = readService.get(Offering.class.getSimpleName(), idFoo);
			final Dto dtoBar = readService.get(Offering.class.getSimpleName(), idBar);
			final Dto dtoBaz = readService.get(Offering.class.getSimpleName(), idBaz);

			assertEquals(childCountFoo, ((List<?>) dtoFoo.get("uniqueServices")).size());
			assertEquals(childCountBar, ((List<?>) dtoBar.get("uniqueServices")).size());
			assertTrue(((List<?>)dtoBaz.get("uniqueServices")).isEmpty());
		}
	}
}
