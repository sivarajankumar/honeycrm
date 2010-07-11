package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@Searchable
@PersistenceCapable
@ListViewable( { "contactId", "probability", "amount" })
@DetailViewable( { "contactId,amount", "phase,probability", "reasonClosed", "description" })
@Quicksearchable( { "contactId", "amount" })
public class Opportunity extends AbstractEntity {
	@FieldRelateAnnotation(Contact.class)
	@Label("Contact")
	private long contactId;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	private double amount;

	@Label("Probability")
	@FieldIntegerAnnotation(0)
	private int probability;

	@FieldEnumAnnotation( { "Open", "Cold Call", "Closed Won", "Closed Lost" })
	@Label("Phase")
	@SearchableProperty
	private String phase;

	@Label("Reason Closed")
	@FieldEnumAnnotation( { "No Time", "Too Expensive" })
	@SearchableProperty
	private String reasonClosed;

	@FieldTextAnnotation
	@Label("Description")
	@SearchableProperty
	private String description;

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
