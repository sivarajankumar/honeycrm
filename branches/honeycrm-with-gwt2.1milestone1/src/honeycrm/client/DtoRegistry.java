package honeycrm.client;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.DtoAccount;
import honeycrm.client.dto.DtoContact;
import honeycrm.client.dto.DtoDonation;
import honeycrm.client.dto.DtoEmployee;
import honeycrm.client.dto.DtoMembership;
import honeycrm.client.dto.DtoOffering;
import honeycrm.client.dto.DtoProduct;
import honeycrm.client.dto.DtoProject;
import honeycrm.client.dto.DtoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO avoid cycle creation..
// TODO do this on server side to avoid that new modules have to be added here
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
		list.add(DtoProduct.class);
		list.add(DtoService.class);
		list.add(DtoOffering.class);
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
		map.put(DtoProduct.class, new DtoProduct());
		map.put(DtoService.class, new DtoService());
		map.put(DtoOffering.class, new DtoOffering());
		return map;
	}
}
