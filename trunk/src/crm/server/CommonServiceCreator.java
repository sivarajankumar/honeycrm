package crm.server;

import crm.client.IANA;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.Viewable;

public class CommonServiceCreator extends AbstractCommonService {
	private static final long serialVersionUID = -272641981474976416L;

	public void create(int dtoIndex, Viewable dto) {
		final Object domainObject = copy.copy(dto, getDomainClass(dtoIndex));
		if (null != domainObject) {
			m.makePersistent(domainObject);
		}
	}
	
	public void addDemo(int dtoIndex) {
		final Class<? extends AbstractDto> dtoClass = IANA.unmarshal(dtoIndex);
		
		if (DtoAccount.class == dtoClass) {
			m.makePersistent(DemoDataProvider.account());
		} else if (DtoContact.class == dtoClass) {
			m.makePersistent(DemoDataProvider.contact());
		}
	}
}
