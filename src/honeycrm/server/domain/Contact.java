package honeycrm.server.domain;

import honeycrm.client.dto.Dto;
import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldEmail;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldString;
import honeycrm.client.field.FieldText;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.RelatesTo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "marked", "name", "email", "phone", "accountID" })
@DetailViewable( { "name,accountID", "responsibleId", "email,emailOptedOut", "phone,mobile", "doNotCall,doNotCallExplanation", "city,bankAccountData", "profession,study", "partnerId",
		"child1Id,child2Id" })
@Quicksearchable( { "name" })
public class Contact extends AbstractEntity {
	@Persistent
	@SearchableProperty
	private String city;
	@Persistent
	@SearchableProperty
	private String email;
	@Persistent
	@SearchableProperty
	private String name;
	@Persistent
	@SearchableProperty
	private String phone;
	@Persistent
	@RelatesTo(Account.class)
	private long accountID;
	@Persistent
	private boolean emailOptedOut;
	@Persistent
	@SearchableProperty
	private String mobile;
	@Persistent
	private boolean doNotCall;
	@Persistent
	@SearchableProperty
	private String doNotCallExplanation;
	@Persistent
	@SearchableProperty
	private String bankAccountData;
	@Persistent
	@SearchableProperty
	private String profession;
	@Persistent
	@SearchableProperty
	private String study;
	@Persistent
	@RelatesTo(Contact.class)
	private long partnerId;
	@Persistent
	@RelatesTo(Contact.class)
	private long child1Id;
	@Persistent
	@RelatesTo(Contact.class)
	private long child2Id;
	@Persistent
	@RelatesTo(Employee.class)
	private long responsibleId;

	public Contact() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldString("phone", "Phone"));
		fields.add(new FieldString("city", "City"));
		fields.add(new FieldEmail("email", "E-Mail"));
		fields.add(new FieldRelate("accountID", "account", "Account"));
		fields.add(new FieldBoolean("doNotCall", "Do not call"));
		fields.add(new FieldText("doNotCallExplanation", "Why not call"));
		fields.add(new FieldBoolean("emailOptedOut", "No Mails"));
		fields.add(new FieldString("mobile", "Mobile"));
		fields.add(new FieldText("bankAccountData", "Bank account data"));
		fields.add(new FieldEnum("profession", "Profession", "Student", "Professor", "Scientific Assistant", "Other"));
		fields.add(new FieldEnum("study", "Study area", "None", "Biology", "Physics", "Mathematics", "Computer science"));
		fields.add(new FieldRelate("partnerId", "contact", "Partner"));
		fields.add(new FieldRelate("child1Id", "contact", "First Child"));
		fields.add(new FieldRelate("child2Id", "contact", "Second Child"));
		fields.add(new FieldRelate("responsibleId", "employee", "Responsible"));
	}

	public Contact(final String name) {
		this.name = name;
	}

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
