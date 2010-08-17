package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEmailAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable({ "name", "email" })
@DetailViewable({ "name,accountID", "assignedTo", "email,emailOptedOut", "phone,mobile", "doNotCall,doNotCallExplanation", "city,bankAccountData", "profession,study", "partnerId", "child1Id,child2Id", "secretary" })
@Quicksearchable({ "name" })
public class Contact extends AbstractEntity {
	@SearchableProperty
	@Label("City")
	@FieldStringAnnotation
	public String city;

	@SearchableProperty
	@Label("E Mail")
	@FieldEmailAnnotation
	public String email;

	@SearchableProperty
	@Label("Name")
	@FieldStringAnnotation
	public String name;

	@SearchableProperty
	@Label("Phone")
	@FieldStringAnnotation
	public String phone;

	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	public long accountID;

	@FieldBooleanAnnotation
	@Label("E Mail opted out")
	public boolean emailOptedOut;

	@SearchableProperty
	@Label("Mobile Phone")
	@FieldStringAnnotation
	public String mobile;

	@FieldBooleanAnnotation
	@Label("Do not call")
	public boolean doNotCall;

	@SearchableProperty
	@Label("Do not call explanation")
	@FieldTextAnnotation
	public String doNotCallExplanation;

	@SearchableProperty
	@Label("Bank account data")
	@FieldTextAnnotation
	public String bankAccountData;

	@SearchableProperty
	@Label("Profession")
	@FieldEnumAnnotation({ "Student", "Professor", "Scientific Assistant", "Other" })
	public String profession;

	@SearchableProperty
	@Label("Study area")
	@FieldEnumAnnotation({ "None", "Biology", "Physics", "Mathematics", "Computer science" })
	public String study;

	@Label("Parter")
	@FieldRelateAnnotation(Contact.class)
	public long partnerId;

	@Label("First child")
	@FieldRelateAnnotation(Contact.class)
	public long child1Id;

	@Label("Second child")
	@FieldRelateAnnotation(Contact.class)
	public long child2Id;

	@Label("Secretary")
	@FieldRelateAnnotation(Contact.class)
	public long secretary;
}
