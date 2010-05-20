package crm.server.domain;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Contact extends AbstractEntity {
	@Persistent
	private String city;
	@Persistent
	private String email;
	@Persistent
	private String name;
	@Persistent
	private String phone;
	@Persistent
	private long accountID;
	@Persistent
	private boolean emailOptedOut;
	@Persistent
	private String mobile;
	@Persistent
	private boolean doNotCall;
	@Persistent
	private String doNotCallExplanation;
	@Persistent
	private String bankAccountData;
	@Persistent
	private String profession;
	@Persistent
	private String study;
	@Persistent
	private long partnerId;
	@Persistent
	private long child1Id;
	@Persistent
	private long child2Id;
	@Persistent
	private long responsibleId;

	public Contact() {
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
