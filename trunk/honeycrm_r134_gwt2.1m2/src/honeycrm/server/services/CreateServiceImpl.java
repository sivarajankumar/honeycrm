package honeycrm.server.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.services.CreateService;

public class CreateServiceImpl extends NewService implements CreateService {
	private static final long serialVersionUID = -4355501462039455166L;

	@Override
	public long create(final Dto dto) {
		return db.put(copy.dtoToEntity(dto)).getId();
	}
}
