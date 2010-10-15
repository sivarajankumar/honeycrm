package honeycrm.server.test.medium;

import honeycrm.client.dto.Dto;
import honeycrm.server.DemoDataProvider;

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

			final long idFoo = commonService.create(foo);
			final long idBar = commonService.create(bar);
			final long idBaz = commonService.create(baz);

			final Dto dtoFoo = commonService.get("offering", idFoo);
			final Dto dtoBar = commonService.get("offering", idBar);
			final Dto dtoBaz = commonService.get("offering", idBaz);

			assertEquals(childCountFoo, ((List<?>) dtoFoo.get("services")).size());
			assertEquals(childCountBar, ((List<?>) dtoBar.get("services")).size());
			assertNull(dtoBaz.get("services_objects"));
		}
	}
}
