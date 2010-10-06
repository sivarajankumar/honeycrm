package honeycrm.server.services;

import honeycrm.client.services.DeleteService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteServiceImpl extends NewService implements DeleteService {
	private static final long serialVersionUID = -6940187328929455534L;

	@Override
	public void delete(String kind, long id) {
		db.delete(KeyFactory.createKey(kind, id));
	}
}
