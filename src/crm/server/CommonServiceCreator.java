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

	public long create(int dtoIndex, AbstractDto dto) {
		final AbstractEntity domainObject = (AbstractEntity) copy.copy(dto, getDomainClass(dtoIndex));
		domainObject.setId(null); // explicitly set id to null to force the database to create a new
									// row
		return internalCreate(domainObject);
	}

	// encapsulates setting the created at field on each save
	private long internalCreate(final AbstractEntity domainObject) {
		if (null == domainObject) {
			throw new RuntimeException("Could not create domain object because domainObject was null");
		} else {
			domainObject.setCreatedAt(new Date(System.currentTimeMillis()));
			m.makePersistent(domainObject);
			return domainObject.getId();
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
