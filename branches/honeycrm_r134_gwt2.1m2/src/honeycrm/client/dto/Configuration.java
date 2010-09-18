package honeycrm.client.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class Configuration implements Serializable {
	private static final long serialVersionUID = -8433625915239875401L;
	private Map<String, ModuleDto> moduleDtos;
	private Map<String, Map<String, Set<String>>> relationships;

	public Configuration() {
	}
	
	public Configuration(final Map<String, ModuleDto> dtoModuleData, final Map<String, Map<String, Set<String>>> relationships) {
		this.moduleDtos = dtoModuleData;
		this.relationships = relationships;
	}
	
	public Map<String, ModuleDto> getModuleDtos() {
		return moduleDtos;
	}
	
	public Map<String, Map<String, Set<String>>> getRelationships() {
		return relationships;
	}
}