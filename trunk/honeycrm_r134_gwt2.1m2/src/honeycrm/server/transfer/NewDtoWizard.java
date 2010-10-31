package honeycrm.server.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import honeycrm.client.actions.AbstractAction;
import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.ExtraButton;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldEmail;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldMultiEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldString;
import honeycrm.client.field.FieldTable;
import honeycrm.client.field.FieldText;
import honeycrm.client.field.FieldWebsite;
import honeycrm.client.view.ModuleAction;
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.SearchableEntity;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEmailAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldMultiEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;
import honeycrm.server.domain.decoration.fields.FieldWebsiteAnnotation;

public class NewDtoWizard {
	private static final Logger log = Logger.getLogger(NewDtoWizard.class.getSimpleName());
	
	// private static final Class<?> ENTITIES_SUPERCLASS = AbstractEntity.class;
	// private static final String ENTITIES_PACKAGE = "honeycrm.server.domainNew";

	// private static final Class<?> ENTITIES_SUPERCLASS = honeycrm.server.domain.AbstractEntity.class;
	private static final String ENTITIES_PACKAGE = "honeycrm.server.domain";
	private static final CachingReflectionHelper reflection = new CachingReflectionHelper();
	private static final Configuration configuration;

	static {
		try {
			final HashMap<String, ModuleDto> dtoModuleData = new HashMap<String, ModuleDto>();

			for (final Class<?> domainClass : ReflectionHelper.getClasses(ENTITIES_PACKAGE)) {
				if (Modifier.isAbstract(domainClass.getModifiers())) {
					continue; // skip abstract classes
				}
				dtoModuleData.put(domainClass.getSimpleName(), getEntityMetaData(domainClass));
			}

			configuration = new Configuration(dtoModuleData, null);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot create configuration.");
		}
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private static String getLabel(final Field field) {
		return field.isAnnotationPresent(Label.class) ? field.getAnnotation(Label.class).value() : "";
	}

	private static ModuleDto getEntityMetaData(final Class<?> domainClass) {
		final ModuleDto moduleDto = new ModuleDto();
		final String kind = domainClass.getSimpleName();
		moduleDto.setModule(kind);
		moduleDto.setTitle(kind);
		moduleDto.setHistoryToken(kind);
		moduleDto.setHidden(domainClass.isAnnotationPresent(Hidden.class));
		moduleDto.setFields(getFields(domainClass));

		if (domainClass.isAnnotationPresent(HasExtraButton.class)) {
			final String label = domainClass.getAnnotation(HasExtraButton.class).label();
			final Class<? extends AbstractAction> action = domainClass.getAnnotation(HasExtraButton.class).action();
			final ModuleAction show = domainClass.getAnnotation(HasExtraButton.class).show();

			final ExtraButton b = new ExtraButton();
			b.setLabel(label);
			try {
				b.setAction(action.newInstance());
			} catch (final Exception e) {
				e.printStackTrace();
				log.warning("Cannot instantiate action for " + ExtraButton.class);
			}
			b.setShow(show);

			moduleDto.setExtraButtons(new ExtraButton[] { b });
		} else {
			moduleDto.setExtraButtons(new ExtraButton[0]);
		}

		if (domainClass.isAnnotationPresent(Quicksearchable.class)) {
			moduleDto.setQuickSearchItems(domainClass.getAnnotation(Quicksearchable.class).value());
		}
		if (domainClass.isAnnotationPresent(ListViewable.class)) {
			moduleDto.setListFieldIds(domainClass.getAnnotation(ListViewable.class).value());
		}
		if (domainClass.isAnnotationPresent(DetailViewable.class)) {
			final String[] rows = domainClass.getAnnotation(DetailViewable.class).value();
			final String[][] fields = new String[rows.length][];
			for (int i = 0; i < rows.length; i++) {
				fields[i] = rows[i].split(",");
			}
			moduleDto.setFormFieldIds(fields);
		}
		moduleDto.setRelateFieldMappings(getRelateFieldMappings(domainClass));
		moduleDto.setOneToManyMappings(getOneToManyMappings(domainClass));
		moduleDto.setFulltextFields(getFulltextFields(domainClass));

		return moduleDto;
	}

	private static HashMap<String, String> getRelateFieldMappings(final Class<?> domainClass) {
		final HashMap<String, String> map = new HashMap<String, String>();

		for (final Field field : reflection.getAllFieldsWithAnnotation(domainClass, FieldRelateAnnotation.class)) {
			map.put(field.getName(), field.getAnnotation(FieldRelateAnnotation.class).value().getSimpleName());
		}

		return map;
	}

	private static HashMap<String, String> getOneToManyMappings(final Class<?> domainClass) {
		final HashMap<String, String> map = new HashMap<String, String>();

		for (final Field field : reflection.getAllFieldsWithAnnotation(domainClass, OneToMany.class)) {
			map.put(field.getName(), field.getAnnotation(OneToMany.class).value().getSimpleName());
		}

		return map;
	}
	
	/**
	 * Returns the names of all fields that should be used for full text search for the given domainClass.
	 * These are all String properties in classes that have been annotated with the SearchableEntity annotation.
	 */
	private static String[] getFulltextFields(final Class<?> domainClass) {
		final ArrayList<String> fields = new ArrayList<String>();

		if (domainClass.isAnnotationPresent(SearchableEntity.class)) {
			for (final Field field : reflection.getAllFields(domainClass)) {
				if (String.class.equals(field.getType())) {
					fields.add(field.getName());
				}
			}
		}

		return fields.toArray(new String[0]);
	}

	private static HashMap<String, AbstractField> getFields(final Class<?> domainClass) {
		final HashMap<String, AbstractField> fields = new HashMap<String, AbstractField>();

		for (final Field field : reflection.getAllFields(domainClass)) {
			final String name = field.getName();

			if (name.startsWith("$") || "id".equals(name) || "serialVersionUID".equals(name)) {
				continue;
			}

			final String label = getLabel(field);

			if (field.isAnnotationPresent(FieldStringAnnotation.class)) {
				fields.put(name, new FieldString(name, label));
			} else if (field.isAnnotationPresent(FieldRelateAnnotation.class)) {
				final String relatedEntity = field.getAnnotation(FieldRelateAnnotation.class).value().getSimpleName();
				fields.put(name, new FieldRelate(name, relatedEntity, label));
			} else if (field.isAnnotationPresent(FieldCurrencyAnnotation.class)) {
				final String defaultValue = field.getAnnotation(FieldCurrencyAnnotation.class).value();
				fields.put(name, new FieldCurrency(name, label, defaultValue));
			} else if (field.isAnnotationPresent(FieldDateAnnotation.class)) {
				fields.put(name, new FieldDate(name, label));
			} else if (field.isAnnotationPresent(FieldTableAnnotation.class)) {
				// TODO pass tableClass to the FieldTable constructor
				// final Class<?> tableClass = field.getAnnotation(FieldTableAnnotation.class).value();
				fields.put(name, new FieldTable(name, label));
			} else if (field.isAnnotationPresent(FieldTextAnnotation.class)) {
				final int width = field.getAnnotation(FieldTextAnnotation.class).width();
				fields.put(name, new FieldText(name, label, width));
			} else if (field.isAnnotationPresent(FieldIntegerAnnotation.class)) {
				final int defaultValue = field.getAnnotation(FieldIntegerAnnotation.class).value();
				fields.put(name, new FieldInteger(name, label, defaultValue));
			} else if (field.isAnnotationPresent(FieldEnumAnnotation.class)) {
				final String[] options = field.getAnnotation(FieldEnumAnnotation.class).value();
				fields.put(name, new FieldEnum(name, label, options));
			} else if (field.isAnnotationPresent(FieldMultiEnumAnnotation.class)) {
				final String[] options = field.getAnnotation(FieldMultiEnumAnnotation.class).value();
				fields.put(name, new FieldMultiEnum(name, label, options));
			} else if (field.isAnnotationPresent(FieldWebsiteAnnotation.class)) {
				fields.put(name, new FieldWebsite(name, label));
			} else if (field.isAnnotationPresent(FieldBooleanAnnotation.class)) {
				fields.put(name, new FieldBoolean(name, label));
			} else if (field.isAnnotationPresent(FieldEmailAnnotation.class)) {
				fields.put(name, new FieldEmail(name, label));
			} else {
				log.warning("Unknown field type: " + domainClass.getSimpleName() + "." + name);
			}
		}

		return fields;
	}
}
