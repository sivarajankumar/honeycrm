package honeycrm.server;

import honeycrm.client.IANA;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.server.domain.AbstractEntity;

import java.util.Date;

/**
 * Is part of the database layer.
 */
public class CommonServiceCreator extends AbstractCommonService {
	private static final long serialVersionUID = -272641981474976416L;

	public long create(int dtoIndex, AbstractDto dto) {
		final AbstractEntity domainObject = (AbstractEntity) copy.copy(dto, getDomainClass(dtoIndex));
		// explicitly set id to null to force the database to create a new row
		domainObject.setId(null);
		return internalCreate(domainObject);
	}

	// encapsulates setting the created at field on each save
	private long internalCreate(final AbstractEntity domainObject) {
		if (null == domainObject) {
			throw new RuntimeException("Could not create domain object because domainObject was null");
		} else {
			domainObject.setCreatedAt(new Date(System.currentTimeMillis()));
			m.makePersistent(domainObject);
			
			if (null == domainObject.getId()) {
				throw new RuntimeException("Could not create domain object: Key is still null");
			} else {
				return domainObject.getId().getId();
			}
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
