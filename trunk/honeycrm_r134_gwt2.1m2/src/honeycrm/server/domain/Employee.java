package honeycrm.server.domain;

import java.util.Date;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEmailAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domainNew.SearchableEntity;

@SearchableEntity
@ListViewable({ "active", "name" })
@DetailViewable({ "name,active", "title,phoneOffice", "employedSince", "department,email" })
@Quicksearchable({ "name" })
public class Employee extends AbstractEntity {
	@Label("Name")
	@FieldStringAnnotation
	public String name;
	
	@Label("Employed Since")
	@FieldDateAnnotation
	public Date employedSince;
	
	@Label("Active")
	@FieldBooleanAnnotation
	public boolean active;

	@Label("Department")
	@FieldEnumAnnotation({ "Department 1", "Department 2", "Department 3" })
	public String department;

	@Label("Title")
	@FieldStringAnnotation
	public String title;

	@Label("Phone")
	@FieldStringAnnotation
	public String phoneOffice;

	@Label("E Mail")
	@FieldEmailAnnotation
	public String email;
}
