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

	private String[] listFieldIds;
	private String[][] formFieldIds;
	private String quickSearchItem;
	private String historyToken;
	private String title;
	private String module;
	private Map<String, Object> data = new HashMap<String, Object>();
	private Set<AbstractField> fields = new HashSet<AbstractField>();

	public Dto() {
		data.put("id", null);
	}

	public Object get(final String fieldId) {
		if (data.containsKey(fieldId)) {
			return data.get(fieldId);
		} else {
			Window.alert("Cannot get value " + fieldId + " from dto");
			throw new RuntimeException();
		}
	}

	public void set(final String fieldId, final Object value) {
		//if (data.containsKey(fieldId)) {
			data.put(fieldId, value);
		//} else {
		//	Window.alert("Cannot set value on field " + fieldId + " since field cannot be found in dto");
		///	throw new RuntimeException("Cannot set value on field " + fieldId + " since field cannot be found in dto");
		//}
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Set<AbstractField> getFields() {
		return fields;
	}

	public void setFields(Set<AbstractField> fields) {
		this.fields = fields;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public AbstractField getFieldById(final String id) {
		for (final AbstractField field : fields) {
			if (field.getId().equals(id)) {
				return field;
			}
		}

		// did not find a field with this id. should never reach this point.
		assert false;

		return null;
	}
	
	public void setId(final long id) {
		set("id", id);
	}

	public long getId() {
		return NumberParser.convertToLong(data.get("id"));
	}
	
	public String getHistoryToken() {
		return historyToken;
	}

	public void setHistoryToken(String historyToken) {
		this.historyToken = historyToken;
	}
	
	public static Dto getByModuleName(final List<Dto> dtos, final String moduleName) {
		for (Dto dto: dtos) {
			if (dto.getModule().equals(moduleName)) {
				return dto;
			}
		}
		
		Window.alert("Could not resolve module " + moduleName + " by name");
		throw new RuntimeException();
	}

	public String[] getListFieldIds() {
		return listFieldIds;
	}

	public void setListFieldIds(String[] listFields) {
		this.listFieldIds = listFields;
	}

	public String[][] getFormFieldIds() {
		return formFieldIds;
	}

	public void setFormFieldIds(String[][] formFields) {
		this.formFieldIds = formFields;
	}

	public static boolean isInternalReadOnlyField(String id) {
		final Set<String> internalFields = new HashSet<String>();
		internalFields.add("createdAt");
		internalFields.add("lastUpdatedAt");
		internalFields.add("views");
		internalFields.add("marked");
		
		return internalFields.contains(id);
	}

	public String getQuicksearchItem() {
		return quickSearchItem;
	}
	
	public void setQuicksearchItem(final String quickSearchItem) {
		this.quickSearchItem = quickSearchItem;
	}

	public void setMarked(boolean marked) {
		set("marked", marked);
	}
	
	public boolean getMarked() {
		return (Boolean) get("marked");
	}
	
	public Map<String, Object> getAllData() {
		return data;
	}
}