package honeycrm.server.test.medium;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import honeycrm.server.DemoDataProvider;
import honeycrm.server.domain.Contact;

public class LowLevelApiTest extends DatastoreTest {
	private static final int COUNT = 10;
	private static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();

	public void testGetting() {
		final ArrayList<Key> keys = new ArrayList<Key>(COUNT);

		for (int i = 0; i < COUNT; i++) {
			final Contact c = DemoDataProvider.contact();

			final Entity e = new Entity(Contact.class.getSimpleName());
			e.setProperty("name", c.name);

			keys.add(db.put(e));
		}

		final Map<Key, Entity> entities = db.get(keys);

		assertEquals(COUNT, entities.size());

		final PreparedQuery pq = db.prepare(new Query(Contact.class.getSimpleName()));

		assertEquals(COUNT, pq.countEntities());

		assertTrue(entities.get(keys.get(0)) instanceof Serializable);
	}

	public void testAddingEntityKind() {
		final Entity newKind = new Entity("NewKind");
		final Long value = 23L;
		newKind.setProperty("foo", value);
		db.put(newKind);

		final PreparedQuery pq = db.prepare(new Query("NewKind"));
		assertEquals(1, pq.countEntities());

		final Entity gotEntity = pq.asSingleEntity();
		assertEquals(newKind.getProperty("foo"), gotEntity.getProperty("foo"));
	}
}
