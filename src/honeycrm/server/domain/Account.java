package honeycrm.server.domain;

import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldEmail;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldMultiEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldString;
import honeycrm.client.field.FieldText;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.RelatesTo;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "responsibleId", "phoneOffice", "annualRevenue" })
@DetailViewable( { "name,parentId", "responsibleId", "email,phoneOffice", "website,phoneOther", "rating,annualRevenue", "industry,employees", "tickerSymbol", "shippingAddress", "billingAddress" })
@Quicksearchable( { "name" })
public class Account extends AbstractEntity {
	@SearchableProperty
	private String name;
	@SearchableProperty
	private String shippingAddress;
	@SearchableProperty
	private String billingAddress;
	@SearchableProperty
	private String rating;
	@SearchableProperty
	private String website;
	private int employees;
	@SearchableProperty
	private String tickerSymbol;
	@RelatesTo(Account.class)
	private long parentId;
	private String email;
	@RelatesTo(Employee.class)
	private long responsibleId;
	private String phoneOffice;
	private String phoneOther;
	private String industry;
	private double annualRevenue;

	public Account() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldString("tickerSymbol", "Ticker Symbol"));
		fields.add(new FieldString("phoneOffice", "Phone office"));
		fields.add(new FieldString("phoneOther", "Phone other"));
		fields.add(new FieldString("website", "Website"));
		fields.add(new FieldText("shippingAddress", "Shipping Address"));
		fields.add(new FieldText("billingAddress", "Billing Address"));
		fields.add(new FieldCurrency("annualRevenue", "Annual Revenue", "0"));
		fields.add(new FieldEnum("rating", "Rating", "A", "B", "C", "D", "E", "F"));
		fields.add(new FieldInteger("employees", "Employees"));
		fields.add(new FieldEmail("email", "Email"));
		fields.add(new FieldRelate("responsibleId", "employee", "Responsible"));
		fields.add(new FieldRelate("parentId", "account", "Member Of"));
		fields.add(new FieldMultiEnum("industry", "Industry", "Apparel", "Banking", "Biotechnology", "Chemicals", "Communications", "Construction", "Consulting", "Education", "Electronics", "Energy",
				"Engineering", "Entertainment", "Environmental", "Finance", "Government", "Healthcare", "Hospitality", "Insurance", "Machinery", "Manufacturing", "Media", "Not For Profit",
				"Recreation", "Retail", "Shipping", "Technology", "Telecommunications", "Transportation", "Utilities", "Other"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAnnualRevenue() {
		return annualRevenue;
	}

	public void setAnnualRevenue(double annualRevenue) {
		this.annualRevenue = annualRevenue;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public int getEmployees() {
		return employees;
	}

	public void setEmployees(int employees) {
		this.employees = employees;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getResponsibleId() {
		return responsibleId;
	}

	public void setResponsibleId(long responsibleId) {
		this.responsibleId = responsibleId;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public String getPhoneOther() {
		return phoneOther;
	}

	public void setPhoneOther(String phoneOther) {
		this.phoneOther = phoneOther;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
}
