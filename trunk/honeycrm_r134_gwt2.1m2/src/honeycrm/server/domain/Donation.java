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
import honeycrm.server.domainNew.SearchableEntity;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable( { "marked", "donatorId", "assignedTo", "reaction", "amount" })
@DetailViewable( { "donatorId,kind", "amount", "donatedFor,assignedTo", "reaction,reactedHow", "date", "receiptionDate,projectId" })
@Quicksearchable( { "donatorId", "amount" })
public class Donation extends AbstractEntity {
	@Label("Donator")
	@FieldRelateAnnotation(Contact.class)
	public Key donatorId; // contact

	@Label("Project")
	@FieldRelateAnnotation(Project.class)
	public Key projectId;

	@Label("Donated for")
	@FieldEnumAnnotation( { "Foundation", "Project donation", "Unlinked donation" })
	public String donatedFor; // foundation / project donation / unlinked donation

	@Label("Kind")
	@FieldEnumAnnotation( { "Subscription", "Unique" })
	public String kind; // subscription / once

	@Label("Receiption date")
	@FieldDateAnnotation
	public Date receiptionDate;

	@Label("Reaction")
	@FieldEnumAnnotation( { "Thanked", "Receipt", "Certificate", "No" })
	public String reaction; // thanked / receipt / certificate / no

	@Label("Reaction Channel")
	@FieldMultiEnumAnnotation( { "E-Mail", "Letter", "Phone Call" })
	public String reactedHow; // (=channel) email / mail / phone call

	@FieldDateAnnotation
	@Label("Date")
	public Date date;

	@Label("Amount")
	@FieldCurrencyAnnotation("0")
	public double amount;
}
