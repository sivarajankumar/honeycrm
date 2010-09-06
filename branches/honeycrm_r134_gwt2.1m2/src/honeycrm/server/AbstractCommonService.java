package honeycrm.server;

import honeycrm.server.domain.Bean;
import honeycrm.server.transfer.DtoCopyMachine;
import honeycrm.server.transfer.ReflectionHelper;

import java.util.Collection;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implements very basic functionality for CommonServiceImpl class.
 */
abstract public class AbstractCommonService extends RemoteServiceServlet {
	protected static final Logger log = Logger.getLogger(AbstractCommonService.class.getName());
	private static final long serialVersionUID = -2405965558198509695L;
	protected static final PersistenceManager m = PMF.get().getPersistenceManager();
	protected static final DtoCopyMachine copy = new DtoCopyMachine();
	protected static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	protected static final DomainClassRegistry registry = DomainClassRegistry.instance;

	protected static Class<? extends Bean> getDomainClass(final String dtoIndex) {
		return registry.getDomain(dtoIndex);
	}

	protected static Bean getDomainObject(final String dtoIndex, final long id) {
		final Query query = m.newQuery(getDomainClass(dtoIndex), "id == " + id);
		final Collection<Bean> collection = (Collection<Bean>) query.execute();

		if (1 == collection.size()) {
			return collection.iterator().next();
		} else {
			return null;
		}
	}
}
