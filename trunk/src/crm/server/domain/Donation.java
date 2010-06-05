package crm.server.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
public class Donation extends AbstractEntity {
	@Persistent
	private long employeeId;
	@Persistent
	private long donatorId; // contact
	@Persistent
	private long projectId;
	@Persistent
	@SearchableProperty
	private String donatedFor; // foundation / project donation / unlinked donation
	@Persistent
	@SearchableProperty
	private String kind; // subscription / once
	@Persistent
	private Date receiptionDate;
	@Persistent
	@SearchableProperty
	private String reaction; // thanked / receipt / certificate / no
	@Persistent
	@SearchableProperty
	private String reactedHow; // (=channel) email / mail / phone call
	@Persistent
	private Date date;
	@Persistent
	private double amount;

	public Donation() {
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getDonatorId() {
		return donatorId;
	}

	public void setDonatorId(long donatorId) {
		this.donatorId = donatorId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getDonatedFor() {
		return donatedFor;
	}

	public void setDonatedFor(String donatedFor) {
		this.donatedFor = donatedFor;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Date getReceiptionDate() {
		return receiptionDate;
	}

	public void setReceiptionDate(Date receiptionDate) {
		this.receiptionDate = receiptionDate;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public String getReactedHow() {
		return reactedHow;
	}

	public void setReactedHow(String reactedHow) {
		this.reactedHow = reactedHow;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
