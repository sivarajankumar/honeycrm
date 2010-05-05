package crm.client.dto;

import crm.client.dto.Field.Type;

public class DtoContact extends AbstractDto {
	private static final long serialVersionUID = -5935793335242391078L;

	private String city;
	private String email;
	private String name;
	private String phone;
	private long accountID;

	public static final int INDEX_NAME = 1;
	public static final int INDEX_PHONE = 2;
	public static final int INDEX_EMAIL = 3;
	public static final int INDEX_CITY = 4;
	public static final int INDEX_ACCOUNTID = 5;

	public DtoContact() { // gwt needs it
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new Field(INDEX_PHONE, Type.STRING, "Phone"));
		fields.add(new Field(INDEX_CITY, Type.STRING, "City"));
		fields.add(new Field(INDEX_EMAIL, Type.STRING, "E-Mail"));
		fields.add(new Field(INDEX_ACCOUNTID, Type.RELATE, "Account"));
	}

	public DtoContact(final String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}

	@Override
	public String getTitle() {
		return "Contact";
	}

	// TODO this is absolute crap because this way the Dto class has to know the ui code
	@Override
	public void setFieldValue(final int index, final Object value) {
		switch (index) {
		case INDEX_NAME:
			setName(value.toString());
			break;
		case INDEX_EMAIL:
			setEmail(value.toString());
			break;
		case INDEX_CITY:
			setCity(value.toString());
			break;
		case INDEX_PHONE:
			setPhone(value.toString());
			break;
		case INDEX_ACCOUNTID:
			setAccountID((Long) value);
			break;
		default:
			assert false;
			break;
		}
	}

	@Override
	public int[][] getFormFieldIds() {
		final int[] row1 = new int[] { INDEX_NAME };
		final int[] row2 = new int[] { INDEX_EMAIL };
		final int[] row3 = new int[] { INDEX_PHONE };
		final int[] row4 = new int[] { INDEX_CITY };
		final int[] row5 = new int[] { INDEX_ACCOUNTID };

		return new int[][] { row1, row5, row2, row3, row4 };
	}

	@Override
	public Object getFieldValue(int fieldId) {
		switch (fieldId) {
		case INDEX_NAME:
			return name;
		case INDEX_EMAIL:
			return email;
		case INDEX_CITY:
			return city;
		case INDEX_PHONE:
			return phone;
		case INDEX_ACCOUNTID:
			return accountID;
		default:
			return null;
		}
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_NAME, INDEX_EMAIL, INDEX_PHONE, INDEX_CITY, INDEX_ACCOUNTID };
	}

	@Override
	public String getHistoryToken() {
		return "c";
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_NAME }, new int[] { INDEX_ACCOUNTID } };
	}
}
