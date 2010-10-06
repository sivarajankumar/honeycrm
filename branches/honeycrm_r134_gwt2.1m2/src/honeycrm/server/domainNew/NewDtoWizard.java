package honeycrm.server.domainNew;

import java.lang.reflect.Field;
import java.util.HashMap;

import honeycrm.client.actions.AbstractAction;
import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.ExtraButton;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldString;
import honeycrm.client.field.FieldTable;
import honeycrm.client.field.FieldText;
import honeycrm.client.view.ModuleAction;
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;
import honeycrm.server.transfer.ReflectionHelper;

public class NewDtoWizard {
	private static final CachingReflectionHelper reflection = new CachingReflectionHelper();
	private static final Configuration configuration;

	static {
		try {
			final HashMap<String, ModuleDto> dtoModuleData = new HashMap<String, ModuleDto>();

			for (final Class<?> domainClass : ReflectionHelper.getClassesWithSuperclass("honeycrm.server.domainNew", AbstractEntity.class)) {
				dtoModuleData.put(domainClass.getSimpleName(), getEntityMetaData(domainClass));
			}

			configuration = new Configuration(dtoModuleData, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot create configuration.");
		}
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private static String getLabel(Field field) {
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
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Cannot instantiate action for " + ExtraButton.class);
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
			} else {
				System.err.println("Unknown field type: " + name);
			}
		}

		return fields;
	}
}
