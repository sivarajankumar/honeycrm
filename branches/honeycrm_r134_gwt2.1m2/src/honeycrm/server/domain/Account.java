package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEmailAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldMultiEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldWebsiteAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "phoneOffice" })
@DetailViewable( { "name,parentId", "responsibleId", "email,phoneOffice", "website,phoneOther", "rating,annualRevenue", "industry,employees", "tickerSymbol", "shippingAddress", "billingAddress" })
@Quicksearchable( { "name" })
public class Account extends AbstractEntity {
	@SearchableProperty
	@Label("Name")
	@FieldStringAnnotation
	private String name;
	
	@SearchableProperty
	@Label("Shipping Address")
	@FieldStringAnnotation
	private String shippingAddress;

	@SearchableProperty
	@Label("Billing Address")
	@FieldStringAnnotation
	private String billingAddress;
	
	@SearchableProperty
	@Label("Rating")
	@FieldEnumAnnotation({ "A", "B", "C", "D", "E", "F"})
	private String rating;
	
	@SearchableProperty
	@Label("Website")
	@FieldWebsiteAnnotation
	private String website;
	
	@Label("Employees")
	@FieldIntegerAnnotation(0)
	private int employees;
	
	@SearchableProperty
	@Label("Ticker Symbol")
	@FieldStringAnnotation
	private String tickerSymbol;
	
	@Label("Member Of")
	@FieldRelateAnnotation(Account.class)
	private long parentId;
	
	@Label("E Mail")
	@FieldEmailAnnotation
	private String email;
	
	@Label("Responsible")
	@FieldRelateAnnotation(Employee.class)
	private long responsibleId;
	
	@Label("Phone Office")
	@FieldStringAnnotation
	private String phoneOffice;
	
	@Label("Phone Other")
	@FieldStringAnnotation
	private String phoneOther;
	
	@Label("Industry")
	@FieldMultiEnumAnnotation({"Apparel", "Banking", "Biotechnology", "Chemicals", "Communications", "Construction", "Consulting", "Education", "Electronics", "Energy",
				"Engineering", "Entertainment", "Environmental", "Finance", "Government", "Healthcare", "Hospitality", "Insurance", "Machinery", "Manufacturing", "Media", "Not For Profit",
				"Recreation", "Retail", "Shipping", "Technology", "Telecommunications", "Transportation", "Utilities", "Other"})
	private String industry;
	
	@Label("Annual Revenue")
	@FieldCurrencyAnnotation("0")
	private double annualRevenue;

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
