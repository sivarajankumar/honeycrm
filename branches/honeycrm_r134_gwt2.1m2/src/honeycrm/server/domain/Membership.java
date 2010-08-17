package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "marked", "memberId", "assignedTo", "startDate" })
@DetailViewable( { "memberId,assignedTo", "tiedToPurpose,purpose", "payment,paymentMethod", "startDate,endDate" })
@Quicksearchable( { "memberId", "assignedTo" })
public class Membership extends AbstractEntity {
	@FieldRelateAnnotation(Contact.class)
	@Label("Member")
	public long memberId; // relation to contacts

	@FieldCurrencyAnnotation("0")
	@Label("Contribution")
	public double payment;

	@SearchableProperty
	@FieldEnumAnnotation( { "Yes", "No", "Soon" })
	@Label("Tied to purpose")
	public String tiedToPurpose; // yes, no, 'soon'

	@SearchableProperty
	@FieldTextAnnotation
	@Label("Purpose")
	public String purpose;

	@SearchableProperty
	@Label("Payment method")
	@FieldEnumAnnotation( { "Direct Debit authorisation", "transaction" })
	public String paymentMethod; // Direct Debit authorisation / transaction

	@Label("Start date")
	@FieldDateAnnotation
	public Date startDate;

	@Label("End date")
	@FieldDateAnnotation
	public Date endDate;
}
