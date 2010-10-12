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

import com.google.appengine.api.datastore.Key;

@ListViewable( { "marked", "memberId", "assignedTo", "startDate" })
@DetailViewable( { "memberId,assignedTo", "tiedToPurpose,purpose", "payment,paymentMethod", "startDate,endDate" })
@Quicksearchable( { "memberId", "assignedTo" })
public class Membership extends AbstractEntity {
	@FieldRelateAnnotation(Contact.class)
	@Label("Member")
	public Key memberId; // relation to contacts

	@FieldCurrencyAnnotation("0")
	@Label("Contribution")
	public double payment;

	@FieldEnumAnnotation( { "Yes", "No", "Soon" })
	@Label("Tied to purpose")
	public String tiedToPurpose; // yes, no, 'soon'

	@FieldTextAnnotation
	@Label("Purpose")
	public String purpose;

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
