package crm.server.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Account extends AbstractEntity {
	@Persistent
	private String name;
	@Persistent
	private String address;
	@Persistent
	private Date date;
	@Persistent
	private double annualRevenue;

	public Account() {
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
