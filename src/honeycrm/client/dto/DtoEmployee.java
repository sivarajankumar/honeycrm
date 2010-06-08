package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

public class DtoEmployee extends AbstractDto {
	private static final long serialVersionUID = -2906735286411254274L;
	private String name;
	private boolean active;

	public static final int INDEX_NAME = 1;
	public static final int INDEX_ACTIVE = 2;

	public DtoEmployee() {
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new Field(INDEX_ACTIVE, Type.BOOLEAN, "Active"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getIndexName() {
		return INDEX_NAME;
	}

	public static int getIndexActive() {
		return INDEX_ACTIVE;
	}

	@Override
	protected Object internalGetFieldValue(final int index) {
		switch (index) {
		case INDEX_NAME:
			return name;
		case INDEX_ACTIVE:
			return active;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	public int[][] interalGetFormFieldIds() {
		return new int[][] { new int[] { INDEX_NAME }, new int[] { INDEX_ACTIVE } };
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_ACTIVE, INDEX_NAME };
	}

	@Override
	protected void internalSetFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_ACTIVE:
			setActive((Boolean) value);
			break;
		case INDEX_NAME:
			setName(String.valueOf(value));
			break;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_NAME } };
	}

	@Override
	public String getQuicksearchItem() {
		return name;
	}
}
