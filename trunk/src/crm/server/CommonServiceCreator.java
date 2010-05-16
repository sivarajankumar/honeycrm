package crm.server;

import java.util.Date;

import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.server.domain.AbstractEntity;

/**
 * Is part of the database layer.
 */
public class CommonServiceCreator extends AbstractCommonService {
	private static final long serialVersionUID = -272641981474976416L;

	public void create(int dtoIndex, AbstractDto dto) {
		final AbstractEntity domainObject = (AbstractEntity) copy.copy(dto, getDomainClass(dtoIndex));
		internalCreate(domainObject);
	}

	// encapsulates setting the created at field on each save
	private void internalCreate(final AbstractEntity domainObject) {
		domainObject.setCreatedAt(new Date(System.currentTimeMillis()));
		
		if (null != domainObject) {
			m.makePersistent(domainObject);
		}
	}

	public void addDemo(int dtoIndex) {
		final Class<? extends AbstractDto> dtoClass = IANA.unmarshal(dtoIndex);

		if (DtoAccount.class == dtoClass) {
			internalCreate(DemoDataProvider.account());
		} else if (DtoContact.class == dtoClass) {
			internalCreate(DemoDataProvider.contact());
		}
	}
}
