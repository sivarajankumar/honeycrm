package honeycrm.server.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import honeycrm.client.dto.Dto;
import honeycrm.server.CommonServiceImpl;
import honeycrm.server.domain.Child;
import honeycrm.server.domain.Parent;
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
			final int childCountFoo = r.nextInt(20);
			final int childCountBar = r.nextInt(20);
			final int childCountBaz = r.nextInt(20);

			final Parent foo = getParent(1, childCountFoo);
			final Parent bar = getParent(2, childCountBar);
			final Parent baz = getParent(3, childCountBaz);

			final long idFoo = commonService.create(copy.copy(foo));
			final long idBar = commonService.create(copy.copy(bar));
			final long idBaz = commonService.create(copy.copy(baz));

			final Dto dtoFoo = commonService.get("parent", idFoo);
			final Dto dtoBar = commonService.get("parent", idBar);
			final Dto dtoBaz = commonService.get("parent", idBaz);

			assertEquals(childCountFoo, ((List<?>) dtoFoo.get("children")).size());
			assertEquals(childCountBar, ((List<?>) dtoBar.get("children")).size());
			assertEquals(childCountBaz, ((List<?>) dtoBaz.get("children")).size());
		}
	}

	private Child getChild(final int name) {
		final Child child = new Child();
		child.name = name;
		return child;
	}

	private Parent getParent(final int name, final int childCount) {
		final Parent parent = new Parent();
		parent.name = name;
		parent.children = getChildren(childCount);
		return parent;
	}

	private ArrayList<Child> getChildren(final int childCount) {
		final ArrayList<Child> children = new ArrayList<Child>(childCount);
		for (int i = 0; i < childCount; i++) {
			children.add(getChild(r.nextInt()));
		}
		return children;
	}
}
