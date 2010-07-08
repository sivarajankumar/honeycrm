package honeycrm.server;

import honeycrm.client.dto.ModuleDto;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * The wizard analyzes the domain classes and creates dto descriptions for them based on their fields and the annotations on the classes. Magically it creates a client side description for the domain classes.
 */
public class DtoWizard {
	public static final DtoWizard instance = new DtoWizard();
	private Map<String, ModuleDto> moduleNameToDto;

	private DtoWizard() {
	}

	private Map<String, ModuleDto> internalGetConfiguration() {
		try {
			final Map<String, ModuleDto> configuration = new HashMap<String, ModuleDto>();

			for (final Class<AbstractEntity> domainClass : (Class<AbstractEntity>[]) ReflectionHelper.getClasses("honeycrm.server.domain")) {
				if (!Modifier.isAbstract(domainClass.getModifiers())) {
					final String name = domainClass.getSimpleName().toLowerCase();

					final ModuleDto moduleDto = new ModuleDto();
					moduleDto.setModule(name);
					moduleDto.setHistoryToken(name);
					moduleDto.setTitle(domainClass.getSimpleName());
					moduleDto.setFields(domainClass.newInstance().getFields());

					handleAnnotations(domainClass, moduleDto);

					// this can only be done for retrieved entities
					// dto.setQuicksearchItem()

					configuration.put(name, moduleDto);
				}
			}

			return configuration;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot analyse domain class structure due to a " + e.getClass());
		}
	}

	private void handleAnnotations(final Class<AbstractEntity> domainClass, final ModuleDto moduleDto) {
		moduleDto.setHidden(domainClass.isAnnotationPresent(Hidden.class));
		
		if (domainClass.isAnnotationPresent(ListViewable.class)) {
			moduleDto.setListFieldIds(domainClass.getAnnotation(ListViewable.class).value());
		}

		if (domainClass.isAnnotationPresent(DetailViewable.class)) {
			final String[] rows = domainClass.getAnnotation(DetailViewable.class).value();
			final String[][] formFields = new String[rows.length][];
			for (int i = 0; i < rows.length; i++) {
				formFields[i] = rows[i].split(",");
			}
			moduleDto.setFormFieldIds(formFields);
		}

		if (domainClass.isAnnotationPresent(Quicksearchable.class)) {
			moduleDto.setQuickSearchItems(domainClass.getAnnotation(Quicksearchable.class).value());
		}
	}

	public Map<String, ModuleDto> getDtoConfiguration() {
		if (null == moduleNameToDto) {
			moduleNameToDto = internalGetConfiguration();
		}
		return moduleNameToDto;
	}
}
