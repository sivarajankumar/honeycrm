package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@DetailViewable({ "contactID", "assignedTo", "startDate", "endDate", "services", "recurringServices" })
@ListViewable({ "contactID", "assignedTo", "endDate" })
@Quicksearchable({"contactID"})
public class Contract extends AbstractEntity {
	@Label("Unique Services")
	@FieldTableAnnotation(UniqueService.class)
	@OneToMany(UniqueService.class)
	public List<Key> services;
	
	@Label("Recurring Services")
	@FieldTableAnnotation(RecurringService.class)
	@OneToMany(RecurringService.class)
	public List<Key> recurringServices;

	@Label("Offering")
	@FieldRelateAnnotation(Offering.class)
	public Long offeringID;

	@Label("Contact")
	@FieldRelateAnnotation(Contact.class)
	public Long contactID;

	@Label("Start Date")
	@FieldDateAnnotation
	public Date startDate;

	@Label("End Date")
	@FieldDateAnnotation
	public Date endDate;
}
