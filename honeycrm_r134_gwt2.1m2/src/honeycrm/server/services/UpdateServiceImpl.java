package honeycrm.server.services;

import com.google.appengine.api.datastore.Entity;

import honeycrm.client.dto.Dto;
import honeycrm.client.services.UpdateService;

public class UpdateServiceImpl extends NewService implements UpdateService {
	private static final long serialVersionUID = 4882885833170689326L;

	@Override
	public void update(Dto dto) {
		final Entity e = copy.dtoToEntity(dto);
		db.put(e);
	}
}
