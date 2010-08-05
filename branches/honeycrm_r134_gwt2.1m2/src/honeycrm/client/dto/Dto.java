package honeycrm.client.dto;

import honeycrm.client.field.AbstractField;
import honeycrm.client.misc.NumberParser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;

/**
 * Generic representation of a dto instance filled on the server side.
 */
public class Dto implements Serializable {
	private static final long serialVersionUID = 2098126312789586977L;

	private String module;
	private Map<String, Serializable> data = new HashMap<String, Serializable>();

	public Dto() {
		data.put("id", null);
	}

	public Serializable get(final String fieldId) {
		if (data.containsKey(fieldId)) {
			return data.get(fieldId);
		} else {
			return null;
		}
	}

	public void set(final String fieldId, final Serializable value) {
		data.put(fieldId, value);
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Set<AbstractField> getFields() {
		return DtoModuleRegistry.instance().get(module).getFields();
	}

	public String getTitle() {
		return DtoModuleRegistry.instance().get(module).getTitle();
	}

	public AbstractField getFieldById(final String id) {
		return DtoModuleRegistry.instance().get(module).getFieldById(id);
	}

	public void setId(final long id) {
		set("id", id);
	}

	public long getId() {
		return NumberParser.convertToLong(data.get("id"));
	}

	public String getHistoryToken() {
		return DtoModuleRegistry.instance().get(module).getHistoryToken();
	}

	public static Dto getByModuleName(final List<Dto> dtos, final String moduleName) {
		for (Dto dto : dtos) {
			if (dto.getModule().equals(moduleName)) {
				return dto;
			}
		}

		Window.alert("Could not resolve module " + moduleName + " by name");
		throw new RuntimeException();
	}

	public String[] getListFieldIds() {
		return DtoModuleRegistry.instance().get(module).getListFieldIds();
	}

	public String[][] getFormFieldIds() {
		return DtoModuleRegistry.instance().get(module).getFormFieldIds();
	}

	public static boolean isInternalReadOnlyField(String id) {
		final Set<String> internalFields = new HashSet<String>();
		internalFields.add("createdAt");
		internalFields.add("lastUpdatedAt");
		internalFields.add("views");
		internalFields.add("marked");

		return internalFields.contains(id);
	}

	public String getQuicksearch() {
		final String[] quickSearchItems = getQuicksearchItems();
		String str = "";
		
		for (int i = 0; i < quickSearchItems.length; i++) {
			str += get(quickSearchItems[i]);

			if (i < quickSearchItems.length - 1) {
				str += " ";
			}
		}

		return str;
	}

	public String[] getQuicksearchItems() {
		return DtoModuleRegistry.instance().get(module).getQuickSearchItems();
	}

	public void setMarked(boolean marked) {
		set("marked", marked);
	}

	public boolean getMarked() {
		return (Boolean) get("marked");
	}

	public Map<String, Serializable> getAllData() {
		return data;
	}
}
