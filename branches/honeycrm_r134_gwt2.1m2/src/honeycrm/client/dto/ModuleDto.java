package honeycrm.client.dto;

import honeycrm.client.field.AbstractField;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.user.client.Window;

public class ModuleDto implements Serializable {
	private static final long serialVersionUID = -7089308530920293835L;

	/**
	 * Should this module be visible i.e. represented by a tab in the user interface?
	 */
	private boolean hidden = false;
	private String[] listFieldIds;
	private String[][] formFieldIds;
	private String[] quickSearchItems;
	private ExtraButton[] extraButtons;
	private String historyToken;
	private String title;
	private String module;
	private HashMap<String, AbstractField> fields = new HashMap<String, AbstractField>();
	private HashMap<String, String> relateFieldMappings = new HashMap<String, String>();
	private HashMap<String, String> oneToManyMappings = new HashMap<String, String>();
	private String[] fulltextFields = new String[0];
	
	public ModuleDto() {
	}

	public String[] getListFieldIds() {
		return listFieldIds;
	}
	
	/**
	 * Returns true if searchField is part of the list fields.
	 * TODO Replace listFieldIds with ArrayList<String> -> this way we can use List<?>.containsKey instead of doing this on our own.
	 */
	public boolean isListViewField(final String searchField) {
		for (final String ownField: listFieldIds) {
			if (ownField.equals(searchField)) {
				return true;
			}
		}
		return false;
	}

	public void setListFieldIds(String[] listFieldIds) {
		this.listFieldIds = listFieldIds;
	}

	public String[][] getFormFieldIds() {
		return formFieldIds;
	}

	public void setFormFieldIds(String[][] formFieldIds) {
		this.formFieldIds = formFieldIds;
	}

	public String[] getQuickSearchItems() {
		return quickSearchItems;
	}

	public void setQuickSearchItems(String[] quickSearchItems) {
		this.quickSearchItems = quickSearchItems;
	}

	public String getHistoryToken() {
		return historyToken;
	}

	public void setHistoryToken(String historyToken) {
		this.historyToken = historyToken;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<AbstractField> getFields() {
		return fields.values();
	}

	public void setFields(final HashMap<String, AbstractField> fields) {
		this.fields = fields;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public ExtraButton[] getExtraButtons() {
		return extraButtons;
	}

	public void setExtraButtons(ExtraButton[] extraButtons) {
		this.extraButtons = extraButtons;
	}
	
	public HashMap<String, String> getRelateFieldMappings() {
		return relateFieldMappings;
	}

	public void setRelateFieldMappings(HashMap<String, String> relateFields) {
		this.relateFieldMappings = relateFields;
	}

	public HashMap<String, String> getOneToManyMappings() {
		return oneToManyMappings;
	}

	public void setOneToManyMappings(HashMap<String, String> oneToManyMappings) {
		this.oneToManyMappings = oneToManyMappings;
	}

	public String[] getFulltextFields() {
		return fulltextFields;
	}

	public void setFulltextFields(String[] fulltextFields) {
		this.fulltextFields = fulltextFields;
	}

	public AbstractField getFieldById(final String id) {
		if (fields.containsKey(id)) {
			return fields.get(id);
		} else {
			Window.alert("Could not find field with id " + id + " in " + ModuleDto.class);
			// did not find a field with this id. should never reach this point.
			throw new RuntimeException("Could not find field with id " + id + " in " + ModuleDto.class);
		}
	}
	
	/**
	 * Returns the module dto instance representing the fields that are referenced by the relate field.
	 * E.g. offerings reference unique services with the field "uniqueServices".
	 * getRelatedDto("offering", "uniqueServices") then returns an instance of the module dto for unique services.
	 */
	public static ModuleDto getRelatedDto(final String originatingModuleName, final String relateFieldId) {
		final ModuleDto originatingModule = DtoModuleRegistry.instance().get(originatingModuleName);
		final String relatedModuleName = originatingModule.getRelateFieldMappings().get(relateFieldId);
		return DtoModuleRegistry.instance().get(relatedModuleName);
	}

	public Dto createDto() {
		final Dto dto = new Dto();
		dto.setModule(module);
		return dto;
	}
}
