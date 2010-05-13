package crm.client.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import crm.client.dto.Field.Type;

public abstract class AbstractDto implements Serializable {
	private static final long serialVersionUID = -656461822356703122L;
	// Instances of all existing Dto classes
	private static final Map<Class<? extends AbstractDto>, AbstractDto> map = new HashMap<Class<? extends AbstractDto>, AbstractDto>();
	protected long id;
	protected long views;
	protected boolean marked;
	protected Date createdAt;
	protected Date lastUpdatedAt;
	protected Set<Field> fields = new HashSet<Field>();

	public static final int INDEX_CREATEDAT = -1;
	public static final int INDEX_LASTUPDATEDAT = -2;
	public static final int INDEX_VIEWS = -3;
	public static final int INDEX_MARKED = -4;

	static {
		// add new modules here
		map.put(DtoContact.class, new DtoContact());
		map.put(DtoAccount.class, new DtoAccount());
		map.put(DtoEmployee.class, new DtoEmployee());
	}

	public AbstractDto() {
		fields.add(new Field(INDEX_VIEWS, Type.INTEGER, "Views"));
		fields.add(new Field(INDEX_CREATEDAT, Type.DATE, "Created at"));
		fields.add(new Field(INDEX_LASTUPDATEDAT, Type.DATE, "Last updated at"));
		fields.add(new Field(INDEX_MARKED, Type.BOOLEAN, ""));
		// TODO make it possible to show an empty label without destroying the css
	}

	public void setFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_VIEWS:
			setViews((Long) value);
			break;
		case INDEX_CREATEDAT:
			setCreatedAt((Date) value);
			break;
		case INDEX_LASTUPDATEDAT:
			setLastUpdatedAt((Date) value);
			break;
		case INDEX_MARKED:
			setMarked((Boolean) value);
			break;
		default:
			throw new RuntimeException("Unexpected field index value " + index);
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
			throw new RuntimeException("Unexpected field index value " + index);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Set<Field> getFields() {
		return fields;
	}

	public void setFields(Set<Field> fields) {
		this.fields = fields;
	}

	public static AbstractDto getViewable(final Class<? extends AbstractDto> clazz) {
		assert map.containsKey(clazz);
		return map.get(clazz);
	}

	public static AbstractDto getViewableForHistoryToken(final String historyToken) {
		for (final Class<? extends AbstractDto> clazz : map.keySet()) {
			if (historyToken.equals(map.get(clazz).getHistoryToken())) {
				return map.get(clazz);
			}
		}
		assert false; // should never reach this point..
		return null;
	}

	public Field getFieldById(final int id) {
		for (final Field field : fields) {
			if (id == field.getId()) {
				return field;
			}
		}

		// did not find a field with this id. should never reach this point.
		assert false;

		return null;
	}

	abstract public int[][] getFormFieldIds();

	abstract public String getHistoryToken();

	abstract public int[] getListViewColumnIds();

	abstract public int[][] getSearchFields();

	abstract public String getTitle();
}
