package honeycrm.server.domain;

import honeycrm.client.actions.CreateContractAction;
import honeycrm.client.view.ModuleAction;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable({ "contactID", "deadline" })
@DetailViewable({ "contactID", "assignedTo", "deadline", "services" })
@Quicksearchable({ "contactID" })
@HasExtraButton(label = "Create Contract", action = CreateContractAction.class, show = ModuleAction.DETAIL)
public class Offering extends AbstractEntity {
	@Label("Services")
	@FieldTableAnnotation(Service.class)
	public List<Service> services;

	@Label("Contact")
	@FieldRelateAnnotation(Contact.class)
	public Long contactID;

	@FieldDateAnnotation
	@Label("Deadline")
	public Date deadline;

	@Label("Contract")
	@FieldRelateAnnotation(Contract.class)
	public Long contractID;

	/**
	 * Calculate the sum of all services.
	 */
	public Double getCosts() {
		Double costs = 0.0;
		for (final Service s : services) {
			costs += (s.price - s.discount) * s.quantity;
		}
		return costs;
	}
}
