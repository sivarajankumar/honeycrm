package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
@DetailViewable({ "contactID", "assignedTo", "startDate", "endDate", "services" })
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

	@Label("Services")
	@FieldTableAnnotation(Service.class)
	public List<Service> services;
}
