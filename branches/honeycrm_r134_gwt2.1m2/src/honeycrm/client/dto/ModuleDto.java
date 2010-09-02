package honeycrm.client.dto;

import honeycrm.client.field.AbstractField;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

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
	private HashMap<String, AbstractField> fields = new HashMap<String, AbstractField>(20);

	public ModuleDto() {
	}

	public String[] getListFieldIds() {
		return listFieldIds;
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

	public AbstractField getFieldById(final String id) {
		if (fields.containsKey(id)) {
			return fields.get(id);
		} else {
			// did not find a field with this id. should never reach this point.
			throw new RuntimeException("Could not find field with id " + id + " in " + ModuleDto.class);
		}
	}

	public Dto createDto() {
		final Dto dto = new Dto();
		dto.setModule(module);
		return dto;
	}
}
