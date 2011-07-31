package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.SearchableEntity;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable({ "category", "account", "invoiceAmount" })
@DetailViewable({ "account,invoiceAmount", "invoiceDate,invoiceNumber", "category" })
@Quicksearchable("account")
public class ExternalInvoice extends AbstractEntity {
	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	public Key account;

	@Label("Date")
	@FieldDateAnnotation
	public Date invoiceDate;

	@Label("Number")
	@FieldStringAnnotation
	public String invoiceNumber;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	public double invoiceAmount;

	@Label("Category")
	@FieldEnumAnnotation({ "A", "B", "C" })
	public String category;

}
