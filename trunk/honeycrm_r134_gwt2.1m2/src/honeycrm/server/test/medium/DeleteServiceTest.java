package honeycrm.server.test.medium;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import honeycrm.client.dto.Dto;
import honeycrm.server.services.CreateServiceImpl;
import honeycrm.server.services.DeleteServiceImpl;
import honeycrm.server.services.ReadServiceImpl;

public class DeleteServiceTest extends DatastoreTest {
	private final CreateServiceImpl creator = new CreateServiceImpl();
	private final ReadServiceImpl reader = new ReadServiceImpl();
	private final DeleteServiceImpl deletor = new DeleteServiceImpl();
	private final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	
	public void testDelete() {
		final Dto contact = new Dto("Contact");
		contact.set("name", "Foo");

		deletor.delete("Contact", creator.create(contact));

		final PreparedQuery pq = db.prepare(new Query("Contact"));
		
		assertEquals(0, pq.countEntities());
	}
	
	public void testDeleteOne() {
		final ArrayList<Long> ids = new ArrayList<Long>();
		
		for (int i=0; i<10; i++) {
			final Dto contact = new Dto("Contact");
			contact.set("name", "foo#" + i);
			ids.add(creator.create(contact));
		}
		
		deletor.delete("Contact", ids.get(0));
		
		final PreparedQuery pq = db.prepare(new Query("Contact"));
		
		// only 9 contacts should have survived this
		assertEquals(9, pq.countEntities());
		
		// the deleted entity should not be retrieved because it should have been deleted.
		for (final Entity entity: pq.asIterable()) {
			assertFalse(ids.get(0) == entity.getKey().getId());
		}
		
		assertNull(reader.get("Contact", ids.get(0)));
	}
}
