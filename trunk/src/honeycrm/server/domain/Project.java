package honeycrm.server.domain;

import honeycrm.client.dto.DetailViewable;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListViewable;
import honeycrm.client.dto.RelatesTo;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldString;
import honeycrm.client.field.FieldText;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable({"name","employeeId", "targetSum", "currentSum", "endDate"})
@DetailViewable({"name,employeeId","description,phase","targetSum,currentSum","startDate,endDate"})
public class Project extends AbstractEntity {
	@Persistent
	@SearchableProperty
	private String name;
	@Persistent
	@RelatesTo(Employee.class)
	private long employeeId;
	@Persistent
	@SearchableProperty
	private String description;
	@Persistent
	private double targetSum;
	@Persistent
	private double currentSum;
	@Persistent
	private Date startDate;
	@Persistent
	private Date endDate;
	@Persistent
	@SearchableProperty
	private String phase;

	public Project() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldRelate("employeeId", new Dto(), "Responsible"));
		fields.add(new FieldText("description", "Description"));
		fields.add(new FieldCurrency("targetSum", "Target sum", "0"));
		fields.add(new FieldCurrency("currentSum", "Current sum", "0"));
		fields.add(new FieldDate("startDate", "Start date"));
		fields.add(new FieldDate("endDate", "End date"));
		fields.add(new FieldEnum("phase", "Phase", "not started", "in progress", "closed"));
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
