package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The wizard analyzes the domain classes and creates dto descriptions for them based on their fields and the annotations on the classes.
 * Magically it creates a client side description for the domain classes.
 */
public class DtoWizard {
	public static final DtoWizard instance = new DtoWizard();
	private final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private List<Dto> config;
	private Map<String, Dto> moduleNameToDto;

	private DtoWizard() {
	}

	private List<Dto> internalGetConfiguration() {
		try {
			final List<Dto> configuration = new LinkedList<Dto>();
			
			for (final Class<AbstractEntity> domainClass : (Class<AbstractEntity>[]) ReflectionHelper.getClasses("honeycrm.server.domain")) {
				if (!Modifier.isAbstract(domainClass.getModifiers())) {
					final String name = domainClass.getSimpleName().toLowerCase();

					final Dto dto = new Dto();
					dto.setModule(name);
					dto.setHistoryToken(name);

					for (final Field field : reflectionHelper.getAllFields(domainClass)) {
						// TODO skip fields
						if (field.getName().equals("id") || field.getName().startsWith("jdo") || field.getName().startsWith("$") || Modifier.isStatic(field.getModifiers())) {
							continue;
						}
						dto.set(field.getName(), null);
					}

					if (domainClass.isAnnotationPresent(ListViewable.class)) {
						dto.setListFieldIds(domainClass.getAnnotation(ListViewable.class).value());
					}
					
					if (domainClass.isAnnotationPresent(DetailViewable.class)) {
						final String[] rows = domainClass.getAnnotation(DetailViewable.class).value();
						final String[][] formFields = new String[rows.length][];
						for (int i=0; i<rows.length; i++) {
							formFields[i] = rows[i].split(",");
						}
						dto.setFormFieldIds(formFields);
					}
					
					if (domainClass.isAnnotationPresent(Quicksearchable.class)) {
						dto.setQuicksearchItems(domainClass.getAnnotation(Quicksearchable.class).value());
					}
					
					dto.setTitle(domainClass.getSimpleName());
					dto.setFields(domainClass.newInstance().getFields());
					
					// this can only be done for retrieved entities
					// dto.setQuicksearchItem()

					configuration.add(dto);
				}
			}
			
			return configuration;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private Map<String, Dto> internalGetModuleNameMap() {
		final Map<String, Dto> map = new HashMap<String, Dto>();
		for (final Dto dto: config) {
			map.put(dto.getModule(), dto);
		}
		return map;
	}
	
	public List<Dto> getDtoConfiguration() {
		if (null == config) {
			config = internalGetConfiguration();
		}
		return config;
	}
	
	public Dto getModuleDtoByName(final String name) {
		if (null == moduleNameToDto) {
			moduleNameToDto = internalGetModuleNameMap();
		}
		return moduleNameToDto.get(name);
	}
}
