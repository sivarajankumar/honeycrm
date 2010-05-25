package crm.server.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
public class Project extends AbstractEntity {
	@Persistent
	@SearchableProperty
	private String name;
	@Persistent
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
