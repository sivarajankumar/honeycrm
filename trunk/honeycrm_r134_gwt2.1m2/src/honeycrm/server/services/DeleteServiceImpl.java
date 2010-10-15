package honeycrm.server.services;

import java.util.Set;

import honeycrm.client.services.DeleteService;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteServiceImpl extends NewService implements DeleteService {
	private static final long serialVersionUID = -6940187328929455534L;

	@Override
	public void delete(String kind, long id) {
		db.delete(KeyFactory.createKey(kind, id));
	}

	@Override
	public void deleteAll(String kind, Set<Long> ids) {
		for (final Long id: ids) {
			db.delete(KeyFactory.createKey(kind, id));
		}
	}

	@Override
	public void deleteAll(String kind) {
		throw new RuntimeException("not implemented yet");
	}

	@Override
	public void deleteAllItems() {
		for (final String kind: configuration.keySet()) {
			deleteAll(kind);
		}
	}
}
