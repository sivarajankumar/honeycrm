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
@ListViewable( { "name", "email", "phone", "accountID" })
@DetailViewable( { "name,accountID", "responsibleId", "email,emailOptedOut", "phone,mobile", "doNotCall,doNotCallExplanation", "city,bankAccountData", "profession,study", "partnerId",
		"child1Id,child2Id" })
@Quicksearchable( { "name" })
public class Contact extends AbstractEntity {
	@SearchableProperty
	@Label("City")
	@FieldStringAnnotation
	private String city;

	@SearchableProperty
	@Label("E Mail")
	@FieldEmailAnnotation
	private String email;

	@SearchableProperty
	@Label("Name")
	@FieldStringAnnotation
	private String name;

	@SearchableProperty
	@Label("Phone")
	@FieldStringAnnotation
	private String phone;

	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	private long accountID;

	@FieldBooleanAnnotation
	@Label("E Mail opted out")
	private boolean emailOptedOut;

	@SearchableProperty
	@Label("Mobile Phone")
	@FieldStringAnnotation
	private String mobile;

	@FieldBooleanAnnotation
	@Label("Do not call")
	private boolean doNotCall;

	@SearchableProperty
	@Label("Do not call explanation")
	@FieldTextAnnotation
	private String doNotCallExplanation;

	@SearchableProperty
	@Label("Bank account data")
	@FieldTextAnnotation
	private String bankAccountData;

	@SearchableProperty
	@Label("Profession")
	@FieldEnumAnnotation( { "Student", "Professor", "Scientific Assistant", "Other" })
	private String profession;

	@SearchableProperty
@Label("Study area")
	@FieldEnumAnnotation( { "None", "Biology", "Physics", "Mathematics", "Computer science" })
	private String study;

	@Label("Parter")
	@FieldRelateAnnotation(Contact.class)
	private long partnerId;

	@Label("First child")
	@FieldRelateAnnotation(Contact.class)
	private long child1Id;

	@Label("Second child")
	@FieldRelateAnnotation(Contact.class)
	private long child2Id;

	@Label("Responsible")
	@FieldRelateAnnotation(Employee.class)
	private long responsibleId;

	public String getCity() {
		return city;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}

	public boolean isEmailOptedOut() {
		return emailOptedOut;
	}

	public void setEmailOptedOut(boolean emailOptedOut) {
		this.emailOptedOut = emailOptedOut;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isDoNotCall() {
		return doNotCall;
	}

	public void setDoNotCall(boolean doNotCall) {
		this.doNotCall = doNotCall;
	}

	public String getDoNotCallExplanation() {
		return doNotCallExplanation;
	}

	public void setDoNotCallExplanation(String doNotCallExplanation) {
		this.doNotCallExplanation = doNotCallExplanation;
	}

	public String getBankAccountData() {
		return bankAccountData;
	}

	public void setBankAccountData(String bankAccountData) {
		this.bankAccountData = bankAccountData;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public long getChild1Id() {
		return child1Id;
	}

	public void setChild1Id(long child1Id) {
		this.child1Id = child1Id;
	}

	public long getChild2Id() {
		return child2Id;
	}

	public void setChild2Id(long child2Id) {
		this.child2Id = child2Id;
	}

	public long getResponsibleId() {
		return responsibleId;
	}

	public void setResponsibleId(long responsibleId) {
		this.responsibleId = responsibleId;
	}
}
