package honeycrm.client.dto;

import honeycrm.client.DtoRegistry;
import honeycrm.client.IANA;
import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldMark;
import honeycrm.client.misc.CollectionHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDto implements Serializable {
	private static final long serialVersionUID = -656461822356703122L;
	// Instances of all existing Dto classes
	private static final Map<Class<? extends AbstractDto>, AbstractDto> map = DtoRegistry.instance.getDtoMap();
	protected Long id = -1L; // initialize with -1 indicating that no id has been set yet
	protected long views;
	protected boolean marked;
	protected Date createdAt;
	protected Date lastUpdatedAt;
	protected Set<AbstractField> fields = new HashSet<AbstractField>();

	// TODO use com.google.gwt.user.client.Random instead to generate ids
	public static final int INDEX_CREATEDAT = -1;
	public static final int INDEX_LASTUPDATEDAT = -2;
	public static final int INDEX_VIEWS = -3;
	public static final int INDEX_MARKED = -4;

	public AbstractDto() {
		fields.add(new FieldInteger(INDEX_VIEWS, "Views"));
		fields.add(new FieldDate(INDEX_CREATEDAT, "Created at"));
		fields.add(new FieldDate(INDEX_LASTUPDATEDAT, "Last updated at"));
		fields.add(new FieldMark(INDEX_MARKED, "Marked", IANA.mashal(this.getClass()), id));
		// TODO make it possible to show an empty label without destroying the css
	}

	public void setFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_VIEWS:
		case INDEX_CREATEDAT:
		case INDEX_LASTUPDATEDAT:
			throw new RuntimeException("Settings of builtin fields is not allowed by client. Only server is allowed to set those fields");
		case INDEX_MARKED:
			setMarked((Boolean) value);
			break;
		default:
			internalSetFieldValue(index, value);
		}
	}

	public Object getFieldValue(final int index) {
		switch (index) {
		case INDEX_VIEWS:
			return views;
		case INDEX_CREATEDAT:
			return createdAt;
		case INDEX_LASTUPDATEDAT:
			return lastUpdatedAt;
		case INDEX_MARKED:
			return marked;
		default:
			return internalGetFieldValue(index);
		}
	}

	/**
	 * Returns true if field with given index is an internal field. False otherwise. The internal fields are visible to the client but only the server is allowed to update those fields, i.e. the edit-/create views should not display widgets for changing their values.
	 */
	public static boolean isInternalReadOnlyField(final int index) {
		switch (index) {
		case INDEX_CREATEDAT:
		case INDEX_LASTUPDATEDAT:
		case INDEX_VIEWS:
		case INDEX_MARKED:
			return true;
		default:
			return false;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public Set<AbstractField> getFields() {
		return fields;
	}

	public void setFields(Set<AbstractField> fields) {
		this.fields = fields;
	}

	/*
	 * public static AbstractDto getViewable(final Class<? extends AbstractDto> clazz) { assert map.containsKey(clazz); return map.get(clazz); }
	 */

	public static AbstractDto getDtoFromHistoryToken(final String historyToken) {
		for (final Class<? extends AbstractDto> clazz : map.keySet()) {
			if (historyToken.equals(map.get(clazz).getHistoryToken())) {
				return map.get(clazz);
			}
		}
		assert false; // should never reach this point..
		return null;
	}

	public AbstractField getFieldById(final int id) {
		for (final AbstractField field : fields) {
			if (id == field.getId()) {
				return field;
			}
		}

		// did not find a field with this id. should never reach this point.
		assert false;

		return null;
	}

	public int[][] getFormFieldIds() {
		return CollectionHelper.merge(interalGetFormFieldIds(), new int[][] { new int[] { INDEX_CREATEDAT }, new int[] { INDEX_LASTUPDATEDAT }, new int[] { INDEX_VIEWS }, new int[] { INDEX_MARKED } });
	}

	/**
	 * Return simple class name without "Dto" prefix as default title, e.g., "honeycrm.client.dto.DtoContact" -> "Contact"
	 */
	public String getTitle() {
		return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1 + 3);
	}

	/**
	 * Return lower case simple class name without "Dto" prefix as history token, e.g., "honeycrm.client.dto.DtoContact" -> "Contact"
	 */
	public String getHistoryToken() {
		return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1 + 3).toLowerCase();
	}

	abstract protected void internalSetFieldValue(final int index, final Object value);

	abstract protected Object internalGetFieldValue(final int index);

	abstract protected int[][] interalGetFormFieldIds();

	abstract public int[] getListViewColumnIds();

	abstract public int[][] getSearchFields();

	abstract public String getQuicksearchItem();
}
