package honeycrm.client;

import honeycrm.client.dto.Dto;

import java.util.List;

// TODO avoid cycle creation..
// TODO do this on server side to avoid that new modules have to be added here
public class DtoRegistry {
	public final static DtoRegistry instance = new DtoRegistry();
	private List<Dto> dtos;

	private DtoRegistry() {
	}

	public Dto getDto(final String moduleName) {
		return Dto.getByModuleName(dtos, moduleName); 
	}

	public void setDtos(List<Dto> dtos) {
		this.dtos = dtos;
	}
	
	public List<Dto> getDtos() {
		return dtos;
	}
}
