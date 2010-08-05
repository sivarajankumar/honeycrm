package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEmailAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable({ "active", "name" })
@DetailViewable({ "name,active", "title,phoneOffice", "department,email" })
@Quicksearchable({ "name" })
public class Employee extends AbstractEntity {
	@SearchableProperty
	@Label("Name")
	@FieldStringAnnotation
	private String name;

	@Label("Active")
	@FieldBooleanAnnotation
	private boolean active;

	@Label("Department")
	@FieldEnumAnnotation({ "Department 1", "Department 2", "Department 3" })
	private String department;

	@Label("Title")
	@FieldStringAnnotation
	private String title;

	@Label("Phone")
	@FieldStringAnnotation
	private String phoneOffice;

	@Label("E Mail")
	@FieldEmailAnnotation
	private String email;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(final String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
}
