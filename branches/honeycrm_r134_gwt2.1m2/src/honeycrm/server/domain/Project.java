package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "assignedTo", "targetSum", "currentSum", "endDate" })
@DetailViewable( { "name,assignedTo", "description,phase", "targetSum,currentSum", "startDate,endDate" })
@Quicksearchable( { "name" })
public class Project extends AbstractEntity {
	@SearchableProperty
	@FieldStringAnnotation
	@Label("Name")
	private String name;

	@Label("Description")
	@FieldTextAnnotation
	@SearchableProperty
	private String description;

	@Label("Target sum")
	@FieldCurrencyAnnotation("0")
	private double targetSum;

	@Label("Current sum")
	@FieldCurrencyAnnotation("0")
	private double currentSum;

	@Label("Start date")
	@FieldDateAnnotation
	private Date startDate;

	@Label("End date")
	@FieldDateAnnotation
	private Date endDate;

	@SearchableProperty
	@Label("Phase")
	@FieldEnumAnnotation( { "not started", "in progress", "closed" })
	private String phase;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
