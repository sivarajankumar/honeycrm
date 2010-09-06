package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.Bean;

/**
 * Is part of the database layer.
 */
public class CommonServiceCreator extends AbstractCommonService {
	private static final long serialVersionUID = -272641981474976416L;

	public long create(Dto dto) {
		final Bean domainObject = copy.copy(dto);
		
		if (null == domainObject) {
			throw new RuntimeException("Could not copy dto into domain class");
		} else {
			// explicitly set id to null to force the database to create a new row
			domainObject.setId(null);
			return internalCreate(domainObject);
		}
	}

	// encapsulates setting the created at field on each save
	private long internalCreate(final Bean domainObject) {
		if (null == domainObject) {
			throw new RuntimeException("Could not create domain object because domainObject was null");
		} else {
			// TODO add created at field to bean interface
			// domainObject.createdAt = (new Date(System.currentTimeMillis()));
			m.makePersistent(domainObject);

			if (null == domainObject.getId()) {
				throw new RuntimeException("Could not create domain object: Key is still null");
			} else {
				return domainObject.getId().getId();
			}
		}
	}
}
