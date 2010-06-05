package crm.server;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.server.domain.AbstractEntity;

/**
 * Implements very basic functionality for CommonServiceImpl class.
 */
abstract public class AbstractCommonService extends RemoteServiceServlet {
	protected static final Logger log = Logger.getLogger(AbstractCommonService.class.getName());
	private static final long serialVersionUID = -2405965558198509695L;
	protected static final PersistenceManager m = PMF.get().getPersistenceManager();
	protected static final CopyMachine copy = new CopyMachine();
	protected static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	protected static final DomainClassRegistry registry = DomainClassRegistry.instance;
	
	protected Class<? extends AbstractDto> getDtoClass(final int dtoIndex) {
		return IANA.unmarshal(dtoIndex);
	}

	protected Class<? extends AbstractEntity> getDomainClass(final int dtoIndex) {
		return registry.getDomain(IANA.unmarshal(dtoIndex));
	}

	protected Object getDomainObject(final int dtoIndex, final long id) {
		final Query query = m.newQuery(getDomainClass(dtoIndex), "id == " + id);
		final Collection collection = (Collection) query.execute();

		if (1 == collection.size()) {
			return collection.iterator().next();
		} else {
			return null;
		}
	}

	protected AbstractDto[] getArrayFromQueryResult(final int dtoIndex, final Collection collection) {
		if (collection.isEmpty()) {
			return new AbstractDto[0];
		} else {
			final List<AbstractDto> list = new LinkedList<AbstractDto>();

			for (Object item : collection) {
				list.add((AbstractDto) copy.copy(item, getDtoClass(dtoIndex)));
			}

			return list.toArray(new AbstractDto[0]);
		}
	}
}
