package honeycrm.client.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.user.client.Window;

public class DtoModuleRegistry {
	private static DtoModuleRegistry instance = null;
	private final Configuration configuration;
	
	private DtoModuleRegistry(final Configuration configuration) {
		this.configuration = configuration;
	}

	public static void create(final Configuration configuration) {
		if (null == instance) {
			instance = new DtoModuleRegistry(configuration);
		}
	}

	public static DtoModuleRegistry instance() {
		return instance;
	}

	public ModuleDto get(final String moduleName) {
		if (configuration.getModuleDtos().containsKey(moduleName)) {
			return configuration.getModuleDtos().get(moduleName);
		} else {
			Window.alert("Module '" + moduleName + "' cannot be found in " + DtoModuleRegistry.class);
			throw new RuntimeException("Module '" + moduleName + "' cannot be found in " + DtoModuleRegistry.class);
		}
	}

	public Collection<ModuleDto> getDtos() {
		return configuration.getModuleDtos().values();
	}
	
	public HashMap<String, HashMap<String, HashSet<String>>> getRelationships() {
		return configuration.getRelationships();
	}
	
	/**
	 * Returns a list of all modules that have relationships to the originating module.
	 * E.g. for originatingModule = employees the list should contain projects and opportunities because both reference an employee with the assignedTo field.
	 */
	public ArrayList<String> getRelatedModules(final String originatingModule) {
		final ArrayList<String> list = new ArrayList<String>();
		
		for (final String module: configuration.getRelationships().keySet()) {
			if (configuration.getRelationships().get(module).containsKey(originatingModule)) {
				list.add(module);
			}
		}
		
		return list;
	}
}