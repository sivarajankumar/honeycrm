package honeycrm.server;

import honeycrm.client.dto.DetailViewable;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListViewable;
import honeycrm.server.domain.AbstractEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * The wizard analyzes the domain classes and creates dto descriptions for them based on their fields and the annotations on the classes.
 * Magically it creates a client side description for the domain classes.
 */
public class DtoWizard {
	public static final DtoWizard instance = new DtoWizard();
	private final ReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private List<Dto> config;

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

	public List<Dto> getDtoConfiguration() {
		if (null == config) {
			config = internalGetConfiguration();
		}
		return config;
	}
}
