package crm.client.dto;

import java.util.Date;

import crm.client.dto.Field.Type;

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
		fields.add(new Field(INDEX_ANNUALREVENUE, Type.CURRENCY, "Annual Revenue"));
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
	public String getTitle() {
		return "Account";
	}

	@Override
	public void setFieldValue(final int index, final Object value) {
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
			break;
		}
	}

	@Override
	public int[][] getFormFieldIds() {
		final int[] row1 = new int[] { INDEX_NAME };
		final int[] row2 = new int[] { INDEX_ADDRESS };
		final int[] row3 = new int[] { INDEX_DATE };
		final int[] row4 = new int[] { INDEX_ANNUALREVENUE };

		return new int[][] { row1, row2, row3, row4 };
	}

	@Override
	public Object getFieldValue(final int fieldId) {
		switch (fieldId) {
		case INDEX_DATE:
			return date;
		case INDEX_ADDRESS:
			return address;
		case INDEX_NAME:
			return name;
		case INDEX_ANNUALREVENUE:
			return annualRevenue;
		default:
			assert false; // should never reach this point..
			return null;
		}
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_NAME, INDEX_ADDRESS };
	}

	@Override
	public String getHistoryToken() {
		return "a";
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_NAME } };
	}
}
