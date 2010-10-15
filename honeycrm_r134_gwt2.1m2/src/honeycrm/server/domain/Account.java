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
import honeycrm.server.domainNew.SearchableEntity;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable( { "name", "phoneOffice" })
@DetailViewable( { "name,parentId", "assignedTo", "email,phoneOffice", "website,phoneOther", "rating,annualRevenue", "industry,employees", "tickerSymbol", "shippingAddress", "billingAddress" })
@Quicksearchable( { "name" })
public class Account extends AbstractEntity {
	@Label("Name")
	@FieldStringAnnotation
	public String name;
	
	@Label("Shipping Address")
	@FieldStringAnnotation
	public String shippingAddress;

	@Label("Billing Address")
	@FieldStringAnnotation
	public String billingAddress;
	
	@Label("Rating")
	@FieldEnumAnnotation({ "A", "B", "C", "D", "E", "F"})
	public String rating;
	
	@Label("Website")
	@FieldWebsiteAnnotation
	public String website;
	
	@Label("Employees")
	@FieldIntegerAnnotation(0)
	public int employees;
	
	@Label("Ticker Symbol")
	@FieldStringAnnotation
	public String tickerSymbol;
	
	@Label("Member Of")
	@FieldRelateAnnotation(Account.class)
	public Key parentId;
	
	@Label("E Mail")
	@FieldEmailAnnotation
	public String email;
	
	@Label("Phone Office")
	@FieldStringAnnotation
	public String phoneOffice;
	
	@Label("Phone Other")
	@FieldStringAnnotation
	public String phoneOther;
	
	@Label("Industry")
	@FieldMultiEnumAnnotation({"Apparel", "Banking", "Biotechnology", "Chemicals", "Communications", "Construction", "Consulting", "Education", "Electronics", "Energy",
				"Engineering", "Entertainment", "Environmental", "Finance", "Government", "Healthcare", "Hospitality", "Insurance", "Machinery", "Manufacturing", "Media", "Not For Profit",
				"Recreation", "Retail", "Shipping", "Technology", "Telecommunications", "Transportation", "Utilities", "Other"})
	public String industry;
	
	@Label("Annual Revenue")
	@FieldCurrencyAnnotation("0")
	public double annualRevenue;
}
