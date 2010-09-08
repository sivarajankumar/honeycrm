package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@DetailViewable({ "contactID", "assignedTo", "startDate", "endDate", "services_objects" })
@ListViewable({ "contactID", "assignedTo", "endDate" })
@Quicksearchable({"contactID"})
public class Contract extends AbstractEntity {
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

	@Label("")
	@FieldStringAnnotation
	public List<Key> services_keys;

	@NotPersistent
	@Label("Services")
	@FieldTableAnnotation(Service.class)
	public List<Service> services_objects;
}
