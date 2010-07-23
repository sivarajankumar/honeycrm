package honeycrm.client.dto;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DtoModuleRegistry {
	private static DtoModuleRegistry instance = null;
	private final Map<String, ModuleDto> moduleDtos;
	private final Map<String, Map<String, Set<String>>> relationships;
	
	private DtoModuleRegistry(final Map<String, ModuleDto> dtoModuleData, final Map<String, Map<String, Set<String>>> relationships) {
		this.moduleDtos = dtoModuleData;
		this.relationships = relationships;
	}

	public static void create(final Map<String, ModuleDto> dtoModuleData, final Map<String, Map<String, Set<String>>> relationships) {
		if (null == instance) {
			instance = new DtoModuleRegistry(dtoModuleData, relationships);
		}
	}

	public static DtoModuleRegistry instance() {
		return instance;
	}

	public ModuleDto get(final String moduleName) {
		if (moduleDtos.containsKey(moduleName)) {
			return moduleDtos.get(moduleName);
		} else {
			throw new RuntimeException("Module '" + moduleName + "' cannot be found in " + DtoModuleRegistry.class);
		}
	}

	public Collection<ModuleDto> getDtos() {
		return moduleDtos.values();
	}
	
	public Map<String, Map<String, Set<String>>> getRelationships() {
		return relationships;
	}
}
