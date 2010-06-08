package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

import java.util.Date;


public class DtoAccount extends AbstractDto {
	public static final int INDEX_NAME = 0;
	public static final int INDEX_ADDRESS = 1;
	public static final int INDEX_DATE = 2;
	public static final int INDEX_ANNUALREVENUE = 3;
	private static final long serialVersionUID = 6493752183632133576L;
	private String name;
	private String address;
	private Date date;
	private double annualRevenue;

	public DtoAccount() {
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new Field(INDEX_ADDRESS, Type.STRING, "Address"));
		fields.add(new Field(INDEX_DATE, Type.DATE, "Date"));
		fields.add(new Field(INDEX_ANNUALREVENUE, Type.CURRENCY, "Annual Revenue", "0"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAnnualRevenue() {
		return annualRevenue;
	}

	public void setAnnualRevenue(double annualRevenue) {
		this.annualRevenue = annualRevenue;
	}

	@Override
	protected void internalSetFieldValue(final int index, final Object value) {
		switch (index) {
		case INDEX_NAME:
			setName(value.toString());
			break;
		case INDEX_ADDRESS:
			setAddress(value.toString());
			break;
		case INDEX_DATE:
			// TODO set date from string value
			setDate((Date) value);
			break;
		case INDEX_ANNUALREVENUE:
			// TODO parsing problems?? formatting..
			setAnnualRevenue(Double.valueOf(value.toString()));
			break;
		default:
			assert false;
		}
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		final int[] row1 = new int[] { INDEX_NAME, INDEX_DATE };
		final int[] row11 = new int[]{ INDEX_ANNUALREVENUE };
		final int[] row2 = new int[] { INDEX_ADDRESS };
		return new int[][] { row1, row11, row2 };
	}

	@Override
	protected Object internalGetFieldValue(final int index) {
		switch (index) {
		case INDEX_DATE:
			return date;
		case INDEX_ADDRESS:
			return address;
		case INDEX_NAME:
			return name;
		case INDEX_ANNUALREVENUE:
			return annualRevenue;
		default:
			throw new RuntimeException("Unexpected Index " + index);
		}
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_MARKED, INDEX_NAME, INDEX_ADDRESS };
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
