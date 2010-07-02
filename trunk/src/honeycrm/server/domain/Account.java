package honeycrm.server.domain;

import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldString;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "address", "annualRevenue" })
@DetailViewable( { "name,date", "annualRevenue", "address" })
@Quicksearchable( { "name" })
public class Account extends AbstractEntity {
	@Persistent
	@SearchableProperty
	private String name;
	@Persistent
	@SearchableProperty
	private String address;
	@Persistent
	private Date date;
	@Persistent
	private double annualRevenue;

	public Account() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldString("address", "Address"));
		fields.add(new FieldDate("date", "Date"));
		fields.add(new FieldCurrency("annualRevenue", "Annual Revenue", "0"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAnnualRevenue() {
		return annualRevenue;
	}

	public void setAnnualRevenue(double annualRevenue) {
		this.annualRevenue = annualRevenue;
	}
}
