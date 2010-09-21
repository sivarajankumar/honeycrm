package honeycrm.client.dto;

import honeycrm.client.misc.CollectionHelper;
import honeycrm.client.misc.NumberParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
		return data.get(fieldId);
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

	public void setId(final Long id) {
		set("id", id);
	}

	public long getId() {
		return NumberParser.convertToLong(data.get("id"));
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

	/**
	 * Returns all fields that are "pre-viewable" i.e. all fields with non-empty string values that are stored in this instance. The returned fields are sorted by name.
	 */
	public List<String> getAllPreviewableFieldsSorted() {
		final List<String> previewableFields = new ArrayList<String>();
		final List<String> sortedFieldNames = CollectionHelper.toList(data.keySet());
		Collections.sort(sortedFieldNames);

		for (final String key : sortedFieldNames) {
			final Serializable value = get(key);

			if (!"name".equals(key) && null != value && value instanceof String && !value.toString().isEmpty()) {
				previewableFields.add(key);
			}
		}

		return previewableFields;
	}

	/**
	 * Copies the content of this dto into the destination dto instance. The id field of the source dto (this) is not copied. The ids if list items are nulled i.e. set to null.
	 */
	public Dto copy() {
		final Dto destination = new Dto();
		
		for (final String key : data.keySet()) {
			if (key.equals("id")) {
				continue; // do not copy the id field as well
			}

			destination.set(key, get(key));

			if (destination.get(key) instanceof List<?>) {
				// null the ids of the services to ensure they are created
				for (final Dto item : (List<Dto>) destination.get(key)) {
					item.setId(null);
				}
			}
		}

		return destination;
	}
}
