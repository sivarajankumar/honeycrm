package honeycrm.server.domain;

import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldText;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.RelatesTo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@ListViewable( { "contactId", "amount", "probability" })
@DetailViewable( { "contactId,amount", "phase,probability", "reasonClosed", "description" })
@Quicksearchable( { "contactId", "amount" })
public class Opportunity extends AbstractEntity {
	@RelatesTo(Contact.class)
	@Persistent
	private long contactId;
	@Persistent
	private double amount;
	@Persistent
	private int probability;
	@Persistent
	@SearchableProperty
	private String phase;
	@Persistent
	@SearchableProperty
	private String reasonClosed;
	@Persistent
	@SearchableProperty
	private String description;

	public Opportunity() {
		fields.add(new FieldRelate("contactId", "contact", "Contact"));
		fields.add(new FieldEnum("phase", "Phase", "Open", "Cold Call", "Closed Won", "Closed Lost"));
		fields.add(new FieldInteger("probability", "Probability"));
		fields.add(new FieldCurrency("amount", "Amount"));
		fields.add(new FieldEnum("reasonClosed", "Reason", "No Time", "Too Expensive"));
		fields.add(new FieldText("description", "Description"));
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getReasonClosed() {
		return reasonClosed;
	}

	public void setReasonClosed(String reasonClosed) {
		this.reasonClosed = reasonClosed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
