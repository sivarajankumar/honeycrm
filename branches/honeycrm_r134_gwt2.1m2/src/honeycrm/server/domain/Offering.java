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
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import org.compass.annotations.Searchable;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Searchable
@ListViewable({ "contactID", "deadline" })
@DetailViewable({ "contactID", "assignedTo", "deadline", "services_objects" })
@Quicksearchable({ "contactID" })
@HasExtraButton(label = "Create Contract", action = CreateContractAction.class, show = ModuleAction.DETAIL)
public class Offering extends AbstractEntity {
	// TODO this field basically has neither a label nor a field type for gui
	@Label("")
	@FieldStringAnnotation
	public List<Key> services_keys;

	@NotPersistent
	@Label("Services")
	@FieldTableAnnotation(Service.class)
	public List<Service> services_objects;
	
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
		for (final Service s : services_objects) {
			costs += (s.price - s.discount) * s.quantity;
		}
		return costs;
	}
}
