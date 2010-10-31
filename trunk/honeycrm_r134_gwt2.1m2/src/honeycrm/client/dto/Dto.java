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
	private final HashMap<String, Serializable> data = new HashMap<String, Serializable>();

	public Dto() {
		data.put("id", null);
	}
	
	public Dto(final String module) {
		this();
		this.module = module;
	}

	public Serializable get(final String fieldId) {
		return data.get(fieldId);
	}

	public void set(final String key, final Serializable value) {
		data.put(key, value);
	}

	public String getModule() {
		return module;
	}

	public void setModule(final String module) {
		this.module = module;
	}

	public void setId(final Long id) {
		data.put("id", id);
	}

	public long getId() {
		return NumberParser.convertToLong(data.get("id"));
	}

	public static Dto getByModuleName(final List<Dto> dtos, final String moduleName) {
		for (final Dto dto : dtos) {
			if (dto.getModule().equals(moduleName)) {
				return dto;
			}
		}

		Window.alert("Could not resolve module " + moduleName + " by name");
		throw new RuntimeException();
	}

	public static boolean isInternalReadOnlyField(final String id) {
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

	public void setMarked(final boolean marked) {
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
			final Object value = get(key);

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
		destination.setModule(getModule());

		for (final String key : data.keySet()) {
			if (key.equals("id") || key.endsWith("_resolved")) {
				// do not copy the id / a resolved field
				continue;
			}

			if (get(key) instanceof ArrayList<?>) {
				final ArrayList<Dto> list = (ArrayList<Dto>) get(key);
				for (int i=0; i<list.size(); i++) {
					// null the ids of the services to ensure they are created
					list.set(i, list.get(i).copy());
				}
				destination.set(key, list);
			} else {
				destination.set(key, get(key));
			}
		}

		return destination;
	}
}
