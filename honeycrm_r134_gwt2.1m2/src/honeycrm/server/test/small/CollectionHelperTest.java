package honeycrm.server.test.small;

import honeycrm.client.misc.CollectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class CollectionHelperTest extends TestCase {
	public void testJoin() {
		final String glue = ",";
		final List<String> foo = new ArrayList<String>();
		assertEquals("", CollectionHelper.join(foo, glue));

		foo.add("a");
		assertEquals("a", CollectionHelper.join(foo, glue));

		foo.add("b");
		foo.add("c");
		assertEquals("a" + glue + "b" + glue + "c", CollectionHelper.join(foo, glue));
	}

	public void testToSet() {
		assertTrue(CollectionHelper.toSet(null).isEmpty());
		assertTrue(CollectionHelper.toSet(new String[0]).isEmpty());
		assertEquals(1, CollectionHelper.toSet(new String[] { "foo" }).size());

		final Set<String> set = CollectionHelper.toSet(new String[] { "1", "2" });
		assertEquals(2, set.size());
		assertTrue(set.contains("1"));
		assertTrue(set.contains("2"));
	}
}
