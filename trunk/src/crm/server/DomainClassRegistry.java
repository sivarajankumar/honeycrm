package crm.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.DtoDonation;
import crm.client.dto.DtoEmployee;
import crm.client.dto.DtoMembership;
import crm.client.dto.DtoProject;
import crm.server.domain.AbstractEntity;
import crm.server.domain.Account;
import crm.server.domain.Contact;
import crm.server.domain.Donation;
import crm.server.domain.Employee;
import crm.server.domain.Membership;
import crm.server.domain.Project;

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
