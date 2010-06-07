package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

import java.util.Date;


public class DtoDonation extends AbstractDto {
	private static final long serialVersionUID = 6528143648066271951L;

	@RelatesTo(DtoEmployee.class)
	private long employeeId;
	@RelatesTo(DtoContact.class)
	private long donatorId; // contact
	@RelatesTo(DtoProject.class)
	private long projectId;
	private String donatedFor; // foundation / project donation / unlinked donation
	private String kind; // subscription / once
	private Date receiptionDate;
	private String reaction; // thanked / receipt / certificate / no
	private String reactedHow; // (=channel) email / mail / phone call
	private Date date;
	private double amount;

	public static final int INDEX_EMPLOYEEID = 1;
	public static final int INDEX_DONATORID = 2;
	public static final int INDEX_PROJECTID = 3;
	public static final int INDEX_DONATEDFOR = 4;
	public static final int INDEX_KIND = 5;
	public static final int INDEX_RECEIPTIONDATE = 6;
	public static final int INDEX_REACTION = 7;
	public static final int INDEX_REACTEDHOW = 8;
	public static final int INDEX_DATE = 9;
	public static final int INDEX_AMOUNT = 10;

	public DtoDonation() {
		fields.add(new FieldRelate(INDEX_EMPLOYEEID, DtoEmployee.class, "Employee"));
		fields.add(new FieldRelate(INDEX_DONATORID, DtoContact.class, "Donator"));
		fields.add(new FieldRelate(INDEX_PROJECTID, DtoProject.class, "Project"));
		fields.add(new FieldEnum(INDEX_DONATEDFOR, "Donated for", "Foundation", "Project donation", "Unlinked donation"));
		fields.add(new FieldEnum(INDEX_KIND, "Kind", "Subscription", "Unique"));
		fields.add(new Field(INDEX_RECEIPTIONDATE, Type.DATE, "Receiption date"));
		fields.add(new FieldEnum(INDEX_REACTION, "Reaction", "Thanked", "Receipt", "Certificate", "No"));
		fields.add(new FieldMultiEnum(INDEX_REACTEDHOW, "Reaction channel", "E-Mail", "Letter", "Phone Call"));
		fields.add(new Field(INDEX_DATE, Type.DATE, "Date"));
		fields.add(new Field(INDEX_AMOUNT, Type.CURRENCY, "Amount", "0"));
	}

	@Override
	public String getHistoryToken() {
		return "d";
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_MARKED, INDEX_DONATORID, INDEX_EMPLOYEEID, INDEX_REACTION, INDEX_AMOUNT };
	}

	@Override
	public int[][] getSearchFields() {
		return new int[][] { new int[] { INDEX_EMPLOYEEID } };
	}

	@Override
	public String getTitle() {
		return "Donation";
	}

	@Override
	protected int[][] interalGetFormFieldIds() {
		final int[] row1 = new int[] { INDEX_DONATORID, INDEX_KIND };
		final int[] row2 = new int[] { INDEX_AMOUNT };
		final int[] row3 = new int[] { INDEX_DONATEDFOR, INDEX_EMPLOYEEID };
		final int[] row4 = new int[] { INDEX_REACTION, INDEX_REACTEDHOW };
		final int[] row5 = new int[] { INDEX_DATE };
		final int[] row6 = new int[] { INDEX_RECEIPTIONDATE, INDEX_PROJECTID };
		return new int[][] { row1, row2, row3, row4, row5, row6 };
	}

	@Override
	protected Object internalGetFieldValue(int index) {
		switch (index) {
		case INDEX_EMPLOYEEID:
			return employeeId;
		case INDEX_DONATORID:
			return donatorId;
		case INDEX_PROJECTID:
			return projectId;
		case INDEX_DONATEDFOR:
			return donatedFor;
		case INDEX_KIND:
			return kind;
		case INDEX_RECEIPTIONDATE:
			return receiptionDate;
		case INDEX_REACTION:
			return reaction;
		case INDEX_REACTEDHOW:
			return reactedHow;
		case INDEX_DATE:
			return date;
		case INDEX_AMOUNT:
			return amount;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	protected void internalSetFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_EMPLOYEEID:
			setEmployeeId(Long.parseLong(value.toString()));
			break;
		case INDEX_DONATORID:
			setDonatorId(Long.parseLong(value.toString()));
			break;
		case INDEX_PROJECTID:
			setProjectId(Long.parseLong(value.toString()));
			break;
		case INDEX_DONATEDFOR:
			setDonatedFor(value.toString());
			break;
		case INDEX_KIND:
			setKind(value.toString());
			break;
		case INDEX_RECEIPTIONDATE:
			setReceiptionDate((Date) value);
			break;
		case INDEX_REACTION:
			setReaction(value.toString());
			break;
		case INDEX_REACTEDHOW:
			setReactedHow(value.toString());
			break;
		case INDEX_DATE:
			setDate((Date) value);
			break;
		case INDEX_AMOUNT:
			setAmount(null == value ? 0.0 : Double.parseDouble(value.toString().isEmpty() ? "0" : value.toString()));
			break;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	public long getDonatorId() {
		return donatorId;
	}

	public void setDonatorId(long donatorId) {
		this.donatorId = donatorId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getDonatedFor() {
		return donatedFor;
	}

	public void setDonatedFor(String donatedFor) {
		this.donatedFor = donatedFor;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getReceiptionDate() {
		return receiptionDate;
	}

	public void setReceiptionDate(Date receiptionDate) {
		this.receiptionDate = receiptionDate;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReactedHow() {
		return reactedHow;
	}

	public void setReactedHow(String reactedHow) {
		this.reactedHow = reactedHow;
	}

	@Override
	public String getQuicksearchItem() {
		// TODO relate fields should be resolved i.e. we should be able to access the name of the donator here.
		return getTitle() + " by " + donatorId + " of " + amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
