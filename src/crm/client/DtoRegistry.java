package crm.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.DtoDonation;
import crm.client.dto.DtoEmployee;
import crm.client.dto.DtoMembership;
import crm.client.dto.DtoProject;

// TODO avoid cycle creation..
public class DtoRegistry {
	public final static DtoRegistry instance = new DtoRegistry();

	private DtoRegistry() {
	}

	public List<Class<? extends AbstractDto>> getAllDtoClasses() {
		// TODO this is the current workaround for the cycle problem
		final List<Class<? extends AbstractDto>> list = new ArrayList<Class<? extends AbstractDto>>();
		list.add(DtoContact.class);
		list.add(DtoAccount.class);
		list.add(DtoEmployee.class);
		list.add(DtoMembership.class);
		list.add(DtoDonation.class);
		list.add(DtoProject.class);
		return list;
	}

	public AbstractDto getDto(final Class<? extends AbstractDto> dtoClazz) {
		assert getDtoMap().containsKey(dtoClazz);
		return getDtoMap().get(dtoClazz);
	}

	public Map<Class<? extends AbstractDto>, AbstractDto> getDtoMap() {
		final Map<Class<? extends AbstractDto>, AbstractDto> map = new HashMap<Class<? extends AbstractDto>, AbstractDto>();
		map.put(DtoContact.class, new DtoContact());
		map.put(DtoAccount.class, new DtoAccount());
		map.put(DtoEmployee.class, new DtoEmployee());
		map.put(DtoMembership.class, new DtoMembership());
		map.put(DtoDonation.class, new DtoDonation());
		map.put(DtoProject.class, new DtoProject());
		return map;
	}
}
