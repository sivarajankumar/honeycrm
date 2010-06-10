package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

public class DtoService extends AbstractDto {
	private static final long serialVersionUID = -6969485382441894420L;

	private String name;
	private double price;
	private int quantity;
	private double discount;
	private double sum;

	public static final int INDEX_NAME = 1;
	public static final int INDEX_PRICE = 2;
	public static final int INDEX_QUANTITY = 3;
	public static final int INDEX_DISCOUNT = 4;
	public static final int INDEX_SUM = 5;

	public DtoService() {
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name", "", 200));
		fields.add(new Field(INDEX_PRICE, Type.CURRENCY, "Price", "0.0", 100));
		fields.add(new Field(INDEX_QUANTITY, Type.INTEGER, "Qty", "1", 100));
		fields.add(new Field(INDEX_DISCOUNT, Type.CURRENCY, "Discount", "0.0", 100));
		fields.add(new Field(INDEX_SUM, Type.CURRENCY, "Sum", "0.0", 100, true));
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_NAME, INDEX_PRICE, INDEX_QUANTITY, INDEX_DISCOUNT, INDEX_SUM };
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
		case INDEX_QUANTITY:
			return quantity;
		case INDEX_DISCOUNT:
			return discount;
		case INDEX_SUM:
			return sum;
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
		case INDEX_DISCOUNT:
			setDiscount(Double.parseDouble(value.toString()));
			break;
		case INDEX_SUM:
			setSum(Double.parseDouble(value.toString()));
			break;
		case INDEX_QUANTITY:
			setQuantity(Integer.parseInt(value.toString()));
			break;
		default:
			throw new RuntimeException("Unexpected index " + index);
		}
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		return new int[][] { new int[] { INDEX_NAME }, new int[] { INDEX_PRICE } };
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
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
}
