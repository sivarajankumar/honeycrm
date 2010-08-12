package honeycrm.server.transfer;

import honeycrm.client.actions.AbstractAction;
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
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The wizard analyzes the domain classes and creates dto descriptions for them based on their fields and the annotations on the classes. Magically it creates a client side description for the domain classes.
 */
public class DtoWizard {
	public static final DtoWizard instance = new DtoWizard();
	private Map<String, ModuleDto> moduleNameToDto;
	private CachingReflectionHelper reflectionHelper = new CachingReflectionHelper();

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

					setupFields(domainClass, moduleDto);
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

	private void setupFields(Class<AbstractEntity> domainClass, ModuleDto moduleDto) {
		try {
			final Set<AbstractField> fields = new HashSet<AbstractField>();

			for (final Field field : reflectionHelper.getAllFields(domainClass)) {
				final String name = field.getName();

				if (name.startsWith("jdo") || "id".equals(name)) {
					continue;
				}

				final String label = getLabel(field);

				if (field.isAnnotationPresent(FieldBooleanAnnotation.class)) {
					fields.add(new FieldBoolean(name, label));
				} else if (field.isAnnotationPresent(FieldStringAnnotation.class)) {
					fields.add(new FieldString(name, label));
				} else if (field.isAnnotationPresent(FieldCurrencyAnnotation.class)) {
					fields.add(new FieldCurrency(name, label, field.getAnnotation(FieldCurrencyAnnotation.class).value()));
				} else if (field.isAnnotationPresent(FieldEnumAnnotation.class)) {
					fields.add(new FieldEnum(name, label, field.getAnnotation(FieldEnumAnnotation.class).value()));
				} else if (field.isAnnotationPresent(FieldMultiEnumAnnotation.class)) {
					fields.add(new FieldMultiEnum(name, label, field.getAnnotation(FieldMultiEnumAnnotation.class).value()));
				} else if (field.isAnnotationPresent(FieldEmailAnnotation.class)) {
					fields.add(new FieldEmail(name, label));
				} else if (field.isAnnotationPresent(FieldRelateAnnotation.class)) {
					fields.add(new FieldRelate(name, field.getAnnotation(FieldRelateAnnotation.class).value().getSimpleName().toLowerCase(), label));
				} else if (field.isAnnotationPresent(FieldDateAnnotation.class)) {
					fields.add(new FieldDate(name, label));
				} else if (field.isAnnotationPresent(FieldIntegerAnnotation.class)) {
					fields.add(new FieldInteger(name, label, field.getAnnotation(FieldIntegerAnnotation.class).value()));
				} else if (field.isAnnotationPresent(FieldTextAnnotation.class)) {
					final int width = field.getAnnotation(FieldTextAnnotation.class).width();
					fields.add(new FieldText(name, label, width));
				} else if (field.isAnnotationPresent(FieldTableAnnotation.class)) {
					fields.add(new FieldTable(name, label));
				} else if (field.isAnnotationPresent(FieldWebsiteAnnotation.class)) {
					fields.add(new FieldWebsite(name, label));
				} else {
					System.err.println("field " + field.getName() + " does not define an expected annotation describing its field instance");
				}
			}

			moduleDto.setFields(fields);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the label for field defined by the Label annotation.
	 */
	private String getLabel(final Field field) {
		if (field.isAnnotationPresent(Label.class)) {
			return field.getAnnotation(Label.class).value();
		} else {
			return "";
			// throw new RuntimeException("field '" + field.getName() + "' does not define a field label");
		}
	}

	private void handleAnnotations(final Class<AbstractEntity> domainClass, final ModuleDto moduleDto) {
		moduleDto.setHidden(domainClass.isAnnotationPresent(Hidden.class));
		moduleDto.setExtraButtons(getExtraButtons(domainClass));

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

	private ExtraButton[] getExtraButtons(final Class<AbstractEntity> domainClass) {
		// TODO handle multiple buttons
		try {
			if (domainClass.isAnnotationPresent(HasExtraButton.class)) {
				final String label = domainClass.getAnnotation(HasExtraButton.class).label();
				final Class<? extends AbstractAction> action = domainClass.getAnnotation(HasExtraButton.class).action();

				final ExtraButton b = new ExtraButton();
				b.setLabel(label);
				b.setAction(action.newInstance());
				
				return new ExtraButton[]{b};
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ExtraButton[0];
	}

	public Map<String, ModuleDto> getDtoConfiguration() {
		if (null == moduleNameToDto) {
			moduleNameToDto = internalGetConfiguration();
		}
		return moduleNameToDto;
	}
}
