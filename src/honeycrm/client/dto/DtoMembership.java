package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

import java.util.Date;


public class DtoMembership extends AbstractDto {
	private static final long serialVersionUID = 2957773394150131193L;

	@RelatesTo(DtoContact.class)
	private long memberId; // relation to contacts
	@RelatesTo(DtoEmployee.class)
	private long employeeId; // relation to employees
	private double payment;
	private String tiedToPurpose; // yes, no, 'soon'
	private String purpose;
	private String paymentMethod; // Direct Debit authorisation / transaction
	private Date startDate;
	private Date endDate;

	public static final int INDEX_MEMBERID = 1;
	public static final int INDEX_EMPLOYEEID = 2;
	public static final int INDEX_PAYMENT = 3;
	public static final int INDEX_TIEDTOPURPOSE = 4;
	public static final int INDEX_PURPOSE = 5;
	public static final int INDEX_PAYMENTMETHOD = 6;
	public static final int INDEX_STARTDATE = 7;
	public static final int INDEX_ENDDATE = 8;

	public DtoMembership() {
		fields.add(new FieldRelate(INDEX_MEMBERID, DtoContact.class, "Member"));
		fields.add(new FieldRelate(INDEX_EMPLOYEEID, DtoEmployee.class, "Employee"));
		fields.add(new Field(INDEX_PAYMENT, Type.CURRENCY, "Contribution"));
		fields.add(new FieldEnum(INDEX_TIEDTOPURPOSE, "Tied to purpose", "Yes", "No", "Soon"));
		fields.add(new Field(INDEX_PURPOSE, Type.TEXT, "Purpose"));
		fields.add(new FieldEnum(INDEX_PAYMENTMETHOD, "Payment method", "Direct Debit authorisation", "transaction"));
		fields.add(new Field(INDEX_STARTDATE, Type.DATE, "Start date"));
		fields.add(new Field(INDEX_ENDDATE, Type.DATE, "End date"));
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_MARKED, INDEX_MEMBERID, INDEX_EMPLOYEEID, INDEX_STARTDATE };
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_MEMBERID } };
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		final int[] row1 = { INDEX_MEMBERID, INDEX_EMPLOYEEID };
		final int[] row2 = { INDEX_TIEDTOPURPOSE, INDEX_PURPOSE };
		final int[] row3 = { INDEX_PAYMENT, INDEX_PAYMENTMETHOD };
		final int[] row4 = { INDEX_STARTDATE, INDEX_ENDDATE };
		return new int[][] { row1, row2, row3, row4 };
	}

	@Override
	protected Object internalGetFieldValue(int index) {
		switch (index) {
		case INDEX_MEMBERID:
			return memberId;
		case INDEX_EMPLOYEEID:
			return employeeId;
		case INDEX_PAYMENT:
			return payment;
		case INDEX_TIEDTOPURPOSE:
			return tiedToPurpose;
		case INDEX_PURPOSE:
			return purpose;
		case INDEX_PAYMENTMETHOD:
			return paymentMethod;
		case INDEX_STARTDATE:
			return startDate;
		case INDEX_ENDDATE:
			return endDate;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	protected void internalSetFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_MEMBERID:
			setMemberId(Long.parseLong(value.toString()));
			break;
		case INDEX_EMPLOYEEID:
			setEmployeeId(Long.parseLong(value.toString()));
			break;
		case INDEX_PAYMENT:
			setPayment(Double.parseDouble(value.toString()));
			break;
		case INDEX_TIEDTOPURPOSE:
			setTiedToPurpose((null == value) ? "" : value.toString());
			break;
		case INDEX_PURPOSE:
			setPurpose((null == value) ? "" : value.toString());
			break;
		case INDEX_PAYMENTMETHOD:
			setPaymentMethod((null == value) ? "" : value.toString());
			break;
		case INDEX_STARTDATE:
			setStartDate((Date) value);
			break;
		case INDEX_ENDDATE:
			setEndDate((Date) value);
			break;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public String getTiedToPurpose() {
		return tiedToPurpose;
	}

	public void setTiedToPurpose(String tiedToPurpose) {
		this.tiedToPurpose = tiedToPurpose;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getQuicksearchItem() {
		return getTitle() + " of " + memberId;
	}
}
