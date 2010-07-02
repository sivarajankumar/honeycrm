package honeycrm.server;

import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;
import honeycrm.server.domain.Donation;
import honeycrm.server.domain.Employee;
import honeycrm.server.domain.Membership;
import honeycrm.server.domain.Offering;
import honeycrm.server.domain.Product;
import honeycrm.server.domain.Project;
import honeycrm.server.domain.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DomainClassRegistry {
	public static final DomainClassRegistry instance = new DomainClassRegistry();
	private final Map<String, Class<? extends AbstractEntity>> dtoToDomain = new HashMap<String, Class<? extends AbstractEntity>>();
	private final Map<Class<? extends AbstractEntity>, String> domainToDto = new HashMap<Class<? extends AbstractEntity>, String>();
	
	private DomainClassRegistry() {
		// TODO do this automatically with reflection
		dtoToDomain.put("contact", Contact.class);
		dtoToDomain.put("account", Account.class);
		dtoToDomain.put("employee", Employee.class);
		dtoToDomain.put("membership", Membership.class);
		dtoToDomain.put("donation", Donation.class);
		dtoToDomain.put("project", Project.class);
		dtoToDomain.put("product", Product.class);
		dtoToDomain.put("service", Service.class);
		dtoToDomain.put("offering", Offering.class);
		
		for (final String dto: dtoToDomain.keySet()) {
			domainToDto.put(dtoToDomain.get(dto), dto);
		}
	}
	
	public String getDto(final Class<? extends AbstractEntity> domainClass) {
		assert domainToDto.containsKey(domainClass);
		return domainToDto.get(domainClass);
	}
	
	public Class<? extends AbstractEntity> getDomain(final String dto) {
		assert dtoToDomain.containsKey(dto);
		return dtoToDomain.get(dto);
	}
	
	public Set<Class<? extends AbstractEntity>> getDomainClasses() {
		return domainToDto.keySet();
	}
}
