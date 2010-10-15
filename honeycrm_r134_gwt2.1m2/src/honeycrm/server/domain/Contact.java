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
import honeycrm.server.domainNew.SearchableEntity;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable({ "name", "email" })
@DetailViewable({ "name,accountId", "assignedTo", "email,emailOptedOut", "phone,mobile", "doNotCall,doNotCallExplanation", "city,bankAccountData", "profession,study", "partnerId", "child1Id,child2Id", "secretary" })
@Quicksearchable({ "name" })
public class Contact extends AbstractEntity {
	@Label("City")
	@FieldStringAnnotation
	public String city;

	@Label("E Mail")
	@FieldEmailAnnotation
	public String email;

	@Label("Name")
	@FieldStringAnnotation
	public String name;

	@Label("Phone")
	@FieldStringAnnotation
	public String phone;

	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	public Key accountId;

	@FieldBooleanAnnotation
	@Label("E Mail opted out")
	public boolean emailOptedOut;

	@Label("Mobile Phone")
	@FieldStringAnnotation
	public String mobile;

	@FieldBooleanAnnotation
	@Label("Do not call")
	public boolean doNotCall;

	@Label("Do not call explanation")
	@FieldTextAnnotation
	public String doNotCallExplanation;

	@Label("Bank account data")
	@FieldTextAnnotation
	public String bankAccountData;

	@Label("Profession")
	@FieldEnumAnnotation({ "Student", "Professor", "Scientific Assistant", "Other" })
	public String profession;

	@Label("Study area")
	@FieldEnumAnnotation({ "None", "Biology", "Physics", "Mathematics", "Computer science" })
	public String study;

	@Label("Parter")
	@FieldRelateAnnotation(Contact.class)
	public Key partnerId;

	@Label("First child")
	@FieldRelateAnnotation(Contact.class)
	public Key child1Id;

	@Label("Second child")
	@FieldRelateAnnotation(Contact.class)
	public Key child2Id;

	@Label("Secretary")
	@FieldRelateAnnotation(Contact.class)
	public Key secretary;
}
