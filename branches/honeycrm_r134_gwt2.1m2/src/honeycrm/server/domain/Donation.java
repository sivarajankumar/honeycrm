package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldMultiEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "marked", "donatorId", "assignedTo", "reaction", "amount" })
@DetailViewable( { "donatorId,kind", "amount", "donatedFor,assignedTo", "reaction,reactedHow", "date", "receiptionDate,projectId" })
@Quicksearchable( { "donatorId", "amount" })
public class Donation extends AbstractEntity {
	@Label("Donator")
	@FieldRelateAnnotation(Contact.class)
	private long donatorId; // contact

	@Label("Project")
	@FieldRelateAnnotation(Project.class)
	private long projectId;

	@SearchableProperty
	@Label("Donated for")
	@FieldEnumAnnotation( { "Foundation", "Project donation", "Unlinked donation" })
	private String donatedFor; // foundation / project donation / unlinked donation

	@SearchableProperty
	@Label("Kind")
	@FieldEnumAnnotation( { "Subscription", "Unique" })
	private String kind; // subscription / once

	@Label("Receiption date")
	@FieldDateAnnotation
	private Date receiptionDate;

	@SearchableProperty
	@Label("Reaction")
	@FieldEnumAnnotation( { "Thanked", "Receipt", "Certificate", "No" })
	private String reaction; // thanked / receipt / certificate / no

	@Label("Reaction Channel")
	@SearchableProperty
	@FieldMultiEnumAnnotation( { "E-Mail", "Letter", "Phone Call" })
	private String reactedHow; // (=channel) email / mail / phone call

	@FieldDateAnnotation
	@Label("Date")
	private Date date;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	private double amount;

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
