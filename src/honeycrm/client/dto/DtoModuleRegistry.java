package honeycrm.client.dto;

import java.util.Collection;
import java.util.Map;

public class DtoModuleRegistry {
	private static DtoModuleRegistry instance = null;
	private final Map<String, ModuleDto> moduleDtos;

	private DtoModuleRegistry(final Map<String, ModuleDto> dtoModuleData) {
		this.moduleDtos = dtoModuleData;
	}

	public static void create(final Map<String, ModuleDto> dtoModuleData) {
		if (null == instance) {
			instance = new DtoModuleRegistry(dtoModuleData);
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
}
