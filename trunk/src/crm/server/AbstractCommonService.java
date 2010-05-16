package crm.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.DtoEmployee;
import crm.server.domain.Account;
import crm.server.domain.Contact;
import crm.server.domain.Employee;

/**
 * Implements very basic functionality for CommonServiceImpl class.
 */
abstract public class AbstractCommonService extends RemoteServiceServlet {
	private static final long serialVersionUID = -2405965558198509695L;
	protected static final PersistenceManager m = PMF.get().getPersistenceManager();
	protected static final Map<Class<? extends AbstractDto>, Class> dtoToDomainClass = new HashMap<Class<? extends AbstractDto>, Class>();
	protected static final CopyMachine copy = new CopyMachine();
	protected static final ReflectionHelper reflectionHelper = new CachingReflectionHelper();

	static {
		// TODO do this automatically with reflection
		dtoToDomainClass.put(DtoContact.class, Contact.class);
		dtoToDomainClass.put(DtoAccount.class, Account.class);
		dtoToDomainClass.put(DtoEmployee.class, Employee.class);
	}

	protected Class<? extends AbstractDto> getDtoClass(final int dtoIndex) {
		return IANA.unmarshal(dtoIndex);
	}

	protected Class getDomainClass(final int dtoIndex) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		assert dtoToDomainClass.containsKey(dtoClass);
		return dtoToDomainClass.get(dtoClass);
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