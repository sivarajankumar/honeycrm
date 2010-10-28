package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.SearchableEntity;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import java.util.Date;

@SearchableEntity
@ListViewable( { "name", "assignedTo", "targetSum", "currentSum", "endDate" })
@DetailViewable( { "name,assignedTo", "description,phase", "targetSum,currentSum", "startDate,endDate" })
@Quicksearchable( { "name" })
public class Project extends AbstractEntity {
	@FieldStringAnnotation
	@Label("Name")
	public String name;

	@Label("Description")
	@FieldTextAnnotation
	public String description;

	@Label("Target sum")
	@FieldCurrencyAnnotation("0")
	public double targetSum;

	@Label("Current sum")
	@FieldCurrencyAnnotation("0")
	public double currentSum;

	@Label("Start date")
	@FieldDateAnnotation
	public Date startDate;

	@Label("End date")
	@FieldDateAnnotation
	public Date endDate;

	@Label("Phase")
	@FieldEnumAnnotation( { "not started", "in progress", "closed" })
	public String phase;
}
