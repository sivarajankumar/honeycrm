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
import crm.client.dto.Viewable;
import crm.server.domain.Account;
import crm.server.domain.Contact;
import crm.server.domain.Employee;

/**
 * Implements very basic functionality for CommonServiceImpl class.
 */
abstract public class AbstractCommonService extends RemoteServiceServlet {
	private static final long serialVersionUID = -2405965558198509695L;
	protected static final PersistenceManager m = PMF.get().getPersistenceManager();
	protected static final Map<Class<? extends Viewable>, Class> toDtoMap = new HashMap<Class<? extends Viewable>, Class>();
	protected static final CopyMachine copy = new CopyMachine();

	static {
		// TODO do this automatically with reflection
		toDtoMap.put(DtoContact.class, Contact.class);
		toDtoMap.put(DtoAccount.class, Account.class);
		toDtoMap.put(DtoEmployee.class, Employee.class);
	}

	protected Class<? extends AbstractDto> getDtoClass(final int dtoIndex) {
		return IANA.unmarshal(dtoIndex);
	}
	
	protected Class getDomainClass(final int dtoIndex) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		assert toDtoMap.containsKey(dtoClass);
		return toDtoMap.get(dtoClass);
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
			final List<Viewable> list = new LinkedList<Viewable>();
			
			for (Object item : collection) {
				list.add((Viewable) copy.copy(item, getDtoClass(dtoIndex)));
			}
			
			return list.toArray(new AbstractDto[0]);
		}
	}
}
