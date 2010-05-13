package crm.client.dto;

import crm.client.dto.Field.Type;

public class DtoContact extends AbstractDto {
	private static final long serialVersionUID = -5935793335242391078L;

	private String city;
	private String email;
	private boolean emailOptedOut;
	private String name;
	private String phone;
	private String mobile;
	private boolean doNotCall;
	private String doNotCallExplanation;
	private long accountID;

	// TODO it should not be neccessary do write this by hand..
	// TODO unfortunately we cannot initialize this in a static block with "i++" since the case blocks need constant values. Seems like the static analysis does not recognize this as constants.
	public static final int INDEX_NAME = 0;
	public static final int INDEX_PHONE = 1;
	public static final int INDEX_EMAIL = 2;
	public static final int INDEX_CITY = 3;
	public static final int INDEX_ACCOUNTID = 4;
	public static final int INDEX_EMAILOPTEDOUT = 5;
	public static final int INDEX_DONOTCALLEXPLANATION = 6;
	public static final int INDEX_MOBILE = 7;
	public static final int INDEX_DONOTCALL = 8;

	public DtoContact() { // gwt needs it
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new Field(INDEX_PHONE, Type.STRING, "Phone"));
		fields.add(new Field(INDEX_CITY, Type.STRING, "City"));
		fields.add(new Field(INDEX_EMAIL, Type.EMAIL, "E-Mail"));
		fields.add(new Field(INDEX_ACCOUNTID, Type.RELATE, "Account"));
		fields.add(new Field(INDEX_DONOTCALL, Type.BOOLEAN, "Do not call"));
		fields.add(new Field(INDEX_DONOTCALLEXPLANATION, Type.TEXT, "Why not call"));
		fields.add(new Field(INDEX_EMAILOPTEDOUT, Type.BOOLEAN, "No Mails"));
		fields.add(new Field(INDEX_MOBILE, Type.STRING, "Mobile"));
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

	public boolean isEmailOptedOut() {
		return emailOptedOut;
	}

	public void setEmailOptedOut(boolean emailOptedOut) {
		this.emailOptedOut = emailOptedOut;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isDoNotCall() {
		return doNotCall;
	}

	public void setDoNotCall(boolean doNotCall) {
		this.doNotCall = doNotCall;
	}

	public String getDoNotCallExplanation() {
		return doNotCallExplanation;
	}

	public void setDoNotCallExplanation(String doNotCallExplanation) {
		this.doNotCallExplanation = doNotCallExplanation;
	}

	@Override
	public String getTitle() {
		return "Contact";
	}

	// TODO this is absolute crap because this way the Dto class has to know the ui code
	@Override
	protected void internalSetFieldValue(final int index, final Object value) {
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
		case INDEX_DONOTCALL:
			setDoNotCall((Boolean) value);
			break;
		case INDEX_DONOTCALLEXPLANATION:
			setDoNotCallExplanation(value.toString());
			break;
		case INDEX_EMAILOPTEDOUT:
			setEmailOptedOut((Boolean) value);
			break;
		case INDEX_MOBILE:
			setMobile(value.toString());
			break;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		final int[] row1 = new int[] { INDEX_NAME, INDEX_ACCOUNTID };
		final int[] row2 = new int[] { INDEX_EMAIL, INDEX_EMAILOPTEDOUT };
		final int[] row3 = new int[] { INDEX_PHONE, INDEX_MOBILE };
		final int[] row4 = new int[] { INDEX_CITY };
		final int[] row5 = new int[] { INDEX_DONOTCALL, INDEX_DONOTCALLEXPLANATION };

		return new int[][] { row1, row2, row3, row4, row5 };
	}

	@Override
	protected Object internalGetFieldValue(int index) {
		switch (index) {
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
		case INDEX_DONOTCALL:
			return doNotCall;
		case INDEX_DONOTCALLEXPLANATION:
			return doNotCallExplanation;
		case INDEX_EMAILOPTEDOUT:
			return emailOptedOut;
		case INDEX_MOBILE:
			return mobile;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_MARKED, INDEX_NAME, INDEX_EMAIL, INDEX_PHONE, INDEX_ACCOUNTID };
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
