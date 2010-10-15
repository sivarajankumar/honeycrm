package honeycrm.server;

import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.transfer.DtoCopyMachine;
import honeycrm.server.transfer.ReflectionHelper;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

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

	public static Class<? extends AbstractEntity> getDomainClass(final String dtoIndex) {
		return registry.getDomain(dtoIndex);
	}

	protected static AbstractEntity getDomainObject(final String dtoIndex, final long id) {
		return id > 0 ? (AbstractEntity) m.getObjectById(getDomainClass(dtoIndex), id) : null;
	}
}
