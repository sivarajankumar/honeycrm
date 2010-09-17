package honeycrm.client.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;

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
			Window.alert("Module '" + moduleName + "' cannot be found in " + DtoModuleRegistry.class);
			throw new RuntimeException("Module '" + moduleName + "' cannot be found in " + DtoModuleRegistry.class);
		}
	}

	public Collection<ModuleDto> getDtos() {
		return moduleDtos.values();
	}
	
	public Map<String, Map<String, Set<String>>> getRelationships() {
		return relationships;
	}
	
	/**
	 * Returns a list of all modules that have relationships to the originating module.
	 * E.g. for originatingModule = employees the list should contain projects and opportunities because both reference an employee with the assignedTo field.
	 */
	public ArrayList<String> getRelatedModules(final String originatingModule) {
		final ArrayList<String> list = new ArrayList<String>();
		
		for (final String module: relationships.keySet()) {
			if (relationships.get(module).containsKey(originatingModule)) {
				list.add(module);
			}
		}
		
		return list;
	}
}
