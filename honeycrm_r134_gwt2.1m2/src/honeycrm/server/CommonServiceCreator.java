package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.AbstractEntity;

import java.util.Date;

/**
 * Is part of the database layer.
 */
@Deprecated
public class CommonServiceCreator extends AbstractCommonService {
	private static final long serialVersionUID = -272641981474976416L;

	public long create(final Dto dto) {
		final AbstractEntity domainObject = copy.copy(dto);
		
		if (null == domainObject) {
			throw new RuntimeException("Could not copy dto into domain class");
		} else {
			// explicitly set id to null to force the database to create a new row
			domainObject.id = null;
			return internalCreate(domainObject);
		}
	}

	// encapsulates setting the created at field on each save
	private long internalCreate(final AbstractEntity domainObject) {
		if (null == domainObject) {
			throw new RuntimeException("Could not create domain object because domainObject was null");
		} else {
			domainObject.createdAt = (new Date(System.currentTimeMillis()));
			m.makePersistent(domainObject);

			if (null == domainObject.id) {
				throw new RuntimeException("Could not create domain object: Key is still null");
			} else {
				return domainObject.id.getId();
			}
		}
	}
}
