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
@DetailViewable( { "contactId,amount", "assignedTo", "phase,probability", "reasonClosed", "description" })
@Quicksearchable( { "contactId", "amount" })
public class Opportunity extends AbstractEntity {
	@FieldRelateAnnotation(Contact.class)
	@Label("Contact")
	public long contactId;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	public double amount;

	@Label("Probability")
	@FieldIntegerAnnotation(0)
	public int probability;

	@FieldEnumAnnotation( { "Open", "Cold Call", "Closed Won", "Closed Lost" })
	@Label("Phase")
	@SearchableProperty
	public String phase;

	@Label("Reason Closed")
	@FieldEnumAnnotation( { "No Time", "Too Expensive" })
	@SearchableProperty
	public String reasonClosed;

	@FieldTextAnnotation
	@Label("Description")
	@SearchableProperty
	public String description;
}
