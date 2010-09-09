package honeycrm.server.domain;

import honeycrm.client.actions.CreateContractAction;
import honeycrm.client.view.ModuleAction;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import org.compass.annotations.Searchable;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Searchable
@ListViewable({ "contactID", "deadline" })
@DetailViewable({ "contactID", "assignedTo", "deadline", "services", "recurringServices" })
@Quicksearchable({ "contactID" })
@HasExtraButton(label = "Create Contract", action = CreateContractAction.class, show = ModuleAction.DETAIL)
public class Offering extends AbstractEntity {
	@Label("Unique Services")
	@FieldTableAnnotation(UniqueService.class)
	@OneToMany(UniqueService.class)
	public List<Key> services;

	@Label("Recurring Services")
	@FieldTableAnnotation(RecurringService.class)
	@OneToMany(RecurringService.class)
	public List<Key> recurringServices;
	
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
		return 23.0;
		// TODO hmm, not sure how we want to do that 
/*		Double costs = 0.0;
		for (final UniqueService s : services_objects) {
			costs += (s.price - s.discount) * s.quantity;
		}
		return costs;*/
	}
}
