package honeycrm.server.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.Quicksearchable;

import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

// TODO add scanned invoice as file upload
@Searchable
@PersistenceCapable
@ListViewable({ "category", "account", "invoiceAmount" })
@DetailViewable({ "account,invoiceAmount", "invoiceDate,invoiceNumber", "category" })
@Quicksearchable("account")
public class ExternalInvoice extends AbstractEntity {
	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	public long account;

	@Label("Date")
	@FieldDateAnnotation
	public Date invoiceDate;

	@Label("Number")
	@FieldStringAnnotation
	@SearchableProperty
	public String invoiceNumber;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	public double invoiceAmount;

	@Label("Category")
	@FieldEnumAnnotation({ "A", "B", "C" })
	@SearchableProperty
	public String category;

}
