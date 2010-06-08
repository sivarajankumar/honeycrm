package honeycrm.client.dto;

import honeycrm.client.dto.Field.Type;

import java.util.Date;

public class DtoProject extends AbstractDto {
	private static final long serialVersionUID = -6523180898796265577L;

	private String name;
	@RelatesTo(DtoEmployee.class)
	private long employeeId;
	private String description;
	private double targetSum;
	private double currentSum;
	private Date startDate;
	private Date endDate;
	private String phase;

	public static final int INDEX_NAME = 1;
	public static final int INDEX_EMPLOYEEID = 2;
	public static final int INDEX_DESCRIPTION = 3;
	public static final int INDEX_TARGETSUM = 4;
	public static final int INDEX_CURRENTSUM = 5;
	public static final int INDEX_STARTDATE = 6;
	public static final int INDEX_ENDDATE = 7;
	public static final int INDEX_PHASE = 8;

	public DtoProject() {
		fields.add(new Field(INDEX_NAME, Type.STRING, "Name"));
		fields.add(new FieldRelate(INDEX_EMPLOYEEID, DtoEmployee.class, "Responsible"));
		fields.add(new Field(INDEX_DESCRIPTION, Type.TEXT, "Description"));
		fields.add(new Field(INDEX_TARGETSUM, Type.CURRENCY, "Target sum", "0"));
		fields.add(new Field(INDEX_CURRENTSUM, Type.CURRENCY, "Current sum", "0"));
		fields.add(new Field(INDEX_STARTDATE, Type.DATE, "Start date"));
		fields.add(new Field(INDEX_ENDDATE, Type.DATE, "End date"));
		fields.add(new FieldEnum(INDEX_PHASE, "Phase", "not started", "in progress", "closed"));
	}

	@Override
	public int[] getListViewColumnIds() {
		return new int[] { INDEX_NAME, INDEX_EMPLOYEEID, INDEX_TARGETSUM, INDEX_CURRENTSUM, INDEX_ENDDATE };
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
	protected int[][] interalGetFormFieldIds() {
		final int[] row1 = new int[] { INDEX_NAME, INDEX_EMPLOYEEID };
		final int[] row2 = new int[] { INDEX_DESCRIPTION, INDEX_PHASE };
		final int[] row3 = new int[] { INDEX_TARGETSUM, INDEX_CURRENTSUM };
		final int[] row4 = new int[] { INDEX_STARTDATE, INDEX_ENDDATE };
		return new int[][] { row1, row2, row3, row4 };
	}

	@Override
	protected Object internalGetFieldValue(int index) {
		switch (index) {
		case INDEX_NAME:
			return name;
		case INDEX_EMPLOYEEID:
			return employeeId;
		case INDEX_DESCRIPTION:
			return description;
		case INDEX_TARGETSUM:
			return targetSum;
		case INDEX_CURRENTSUM:
			return currentSum;
		case INDEX_STARTDATE:
			return startDate;
		case INDEX_ENDDATE:
			return endDate;
		case INDEX_PHASE:
			return phase;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	@Override
	protected void internalSetFieldValue(int index, Object value) {
		switch (index) {
		case INDEX_NAME:
			setName(value.toString());
			break;
		case INDEX_EMPLOYEEID:
			setEmployeeId(Long.parseLong(value.toString()));
			break;
		case INDEX_DESCRIPTION:
			setDescription(value.toString());
			break;
		case INDEX_TARGETSUM:
			setTargetSum(Double.parseDouble(value.toString()));
			break;
		case INDEX_CURRENTSUM:
			setCurrentSum(Double.parseDouble(value.toString()));
			break;
		case INDEX_STARTDATE:
			setStartDate((Date) value);
			break;
		case INDEX_ENDDATE:
			setEndDate((Date) value);
			break;
		case INDEX_PHASE:
			setPhase(value.toString());
			break;
		default:
			throw new RuntimeException("Unexpected Index: " + index);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTargetSum() {
		return targetSum;
	}

	public void setTargetSum(double targetSum) {
		this.targetSum = targetSum;
	}

	public double getCurrentSum() {
		return currentSum;
	}

	public void setCurrentSum(double currentSum) {
		this.currentSum = currentSum;
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

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}
}
