package honeycrm.server.domain;

import honeycrm.client.dto.Dto;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldMultiEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.RelatesTo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "marked", "donatorId", "employeeId", "reaction", "amount" })
@DetailViewable( { "donatorId,kind", "amount", "donatedFor,employeeId", "reaction,reactedHow", "date", "receiptionDate,projectId" })
@Quicksearchable( { "donatorId", "amount" })
public class Donation extends AbstractEntity {
	@Persistent
	@RelatesTo(Employee.class)
	private long employeeId;
	@Persistent
	@RelatesTo(Contact.class)
	private long donatorId; // contact
	@Persistent
	@RelatesTo(Project.class)
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
		fields.add(new FieldRelate("employeeId", "employee", "Employee"));
		fields.add(new FieldRelate("donatorId", "contact", "Donator"));
		fields.add(new FieldRelate("projectId", "project", "Project"));
		fields.add(new FieldEnum("donatedFor", "Donated for", "Foundation", "Project donation", "Unlinked donation"));
		fields.add(new FieldEnum("kind", "Kind", "Subscription", "Unique"));
		fields.add(new FieldDate("receiptionDate", "Receiption date"));
		fields.add(new FieldEnum("reaction", "Reaction", "Thanked", "Receipt", "Certificate", "No"));
		fields.add(new FieldMultiEnum("reactedHow", "Reaction channel", "E-Mail", "Letter", "Phone Call"));
		fields.add(new FieldDate("date", "Date"));
		fields.add(new FieldCurrency("amount", "Amount", "0"));
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
