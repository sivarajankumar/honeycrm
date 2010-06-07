package honeycrm.server;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.client.dto.DtoDonation;
import honeycrm.client.dto.DtoEmployee;
import honeycrm.client.dto.DtoMembership;
import honeycrm.client.dto.DtoProject;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.Account;
import honeycrm.server.domain.Contact;
import honeycrm.server.domain.Donation;
import honeycrm.server.domain.Employee;
import honeycrm.server.domain.Membership;
import honeycrm.server.domain.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DomainClassRegistry {
	public static final DomainClassRegistry instance = new DomainClassRegistry();
	private final Map<Class<? extends AbstractDto>, Class<? extends AbstractEntity>> dtoToDomain = new HashMap<Class<? extends AbstractDto>, Class<? extends AbstractEntity>>();
	private final Map<Class<? extends AbstractEntity>, Class<? extends AbstractDto>> domainToDto = new HashMap<Class<? extends AbstractEntity>, Class<? extends AbstractDto>>();
	
	private DomainClassRegistry() {
		// TODO do this automatically with reflection
		dtoToDomain.put(DtoContact.class, Contact.class);
		dtoToDomain.put(DtoAccount.class, Account.class);
		dtoToDomain.put(DtoEmployee.class, Employee.class);
		dtoToDomain.put(DtoMembership.class, Membership.class);
		dtoToDomain.put(DtoDonation.class, Donation.class);
		dtoToDomain.put(DtoProject.class, Project.class);
		
		for (final Class<? extends AbstractDto> dto: dtoToDomain.keySet()) {
			domainToDto.put(dtoToDomain.get(dto), dto);
		}
	}
	
	public Class<? extends AbstractDto> getDto(final Class<? extends AbstractEntity> domainClass) {
		assert domainToDto.containsKey(domainClass);
		return domainToDto.get(domainClass);
	}
	
	public Class<? extends AbstractEntity> getDomain(final Class<? extends AbstractDto> dto) {
		assert dtoToDomain.containsKey(dto);
		return dtoToDomain.get(dto);
	}
	
	public Set<Class<? extends AbstractEntity>> getDomainClasses() {
		return domainToDto.keySet();
	}
}
