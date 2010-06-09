package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

public class DtoService extends AbstractDto {
	private static final long serialVersionUID = -6969485382441894420L;

	private String name;
	private double price;
	
	public static final int INDEX_NAME = 1;
	public static final int INDEX_PRICE = 2;

	public DtoService() {
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new Field(INDEX_PRICE, Type.CURRENCY, "Price"));
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_NAME, INDEX_PRICE };
	}

	@Override
	public String getQuicksearchItem() {
		return name;
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_NAME } };
	}

	@Override
	protected Object internalGetFieldValue(int index) {
		switch (index) {
		case INDEX_NAME:
			return name;
		case INDEX_PRICE:
			return price;
		default:
			throw new RuntimeException("Unexpected index " + index);
		}
	}

	@Override
	protected void internalSetFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_NAME:
			setName(value.toString());
			break;
		case INDEX_PRICE:
			setPrice(Double.parseDouble(value.toString()));
			break;
		default:
			throw new RuntimeException("Unexpected index " + index);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		return new int[][] { new int[] { INDEX_NAME }, new int[] { INDEX_PRICE } };
	}
}
