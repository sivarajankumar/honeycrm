package honeycrm.client.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Configuration implements Serializable {
	private static final long serialVersionUID = -8433625915239875401L;
	private HashMap<String, ModuleDto> moduleDtos;
	private HashMap<String, HashMap<String, HashSet<String>>> relationships;

	public Configuration() {
	}
	
	public Configuration(final HashMap<String, ModuleDto> dtoModuleData, final HashMap<String, HashMap<String, HashSet<String>>> relationships) {
		this.moduleDtos = dtoModuleData;
		// TODO until the code that relies on this datastructure has been transformed, we calculate the required datastructure here.
		this.relationships = calculateRelationships();
	}
	
	private HashMap<String, HashMap<String, HashSet<String>>> calculateRelationships() {
		final HashMap<String, HashMap<String, HashSet<String>>> calculatedRelationships = new HashMap<String, HashMap<String,HashSet<String>>>();
		
		for (final ModuleDto moduleDto: moduleDtos.values()) {
			final HashMap<String, HashSet<String>> relationsOfModule = new HashMap<String, HashSet<String>>();
			
			for (final Map.Entry<String, String> entry: moduleDto.getRelateFieldMappings().entrySet()) {
				final String fieldName = entry.getKey();
				final String relatedModule = entry.getValue();
				
				if (!relationsOfModule.containsKey(relatedModule)) {
					relationsOfModule.put(relatedModule, new HashSet<String>());
				}

				relationsOfModule.get(relatedModule).add(fieldName);
			}
			
			calculatedRelationships.put(moduleDto.getModule(), relationsOfModule);
		}
		
		return calculatedRelationships;
	}

	public HashMap<String, ModuleDto> getModuleDtos() {
		return moduleDtos;
	}
	
	public HashMap<String, HashMap<String, HashSet<String>>> getRelationships() {
		return relationships;
	}
}