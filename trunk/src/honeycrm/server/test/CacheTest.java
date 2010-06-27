package honeycrm.server.test;

import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.client.prefetch.CacheKey;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

public class CacheTest extends TestCase {
	private final Random r = new Random(System.currentTimeMillis());

	public void testKey() {
		final long someRandomLong = r.nextLong();

		assertFalse(new Object[] { 1, 2 }.hashCode() == new Object[] { 1, 2 }.hashCode());
		
		final CacheKey c = new CacheKey(DtoContact.class, DtoAccount.class, someRandomLong);
		final CacheKey d = new CacheKey(DtoContact.class, DtoAccount.class, someRandomLong);
		final CacheKey e = new CacheKey(DtoContact.class, DtoAccount.class, someRandomLong + 1);

		assertTrue(c.equals(d));
		assertFalse(c.equals(e));
		assertFalse(d.equals(e));

		final Set<CacheKey> set = new HashSet<CacheKey>();
		assertTrue(set.isEmpty());

		// add 1st item
		set.add(c);
		assertEquals(1, set.size());
		set.add(c);
		assertEquals(1, set.size());

		// add 2nd item
		set.add(e);
		set.add(d);
		set.add(d);
		set.add(e);
		set.add(new CacheKey(DtoContact.class, DtoAccount.class, someRandomLong + 1));
		set.add(new CacheKey(DtoContact.class, DtoAccount.class, someRandomLong));
		assertEquals(2, set.size());
	}
}
