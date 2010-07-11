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

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "contactId", "deadline" })
@DetailViewable( { "contactId", "deadline", "services" })
@Quicksearchable( { "contactId", "deadline" })
public class Offering extends AbstractEntity {
	@Label("Services")
	@FieldTableAnnotation(Service.class)
	private List<Service> services;
	
	@Label("Contact")
	@FieldRelateAnnotation(Contact.class)
	private Long contactId;
	
	@FieldDateAnnotation
	@Label("Deadline")
	private Date deadline;

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	/**
	 * Calculate the sum of all services.
	 */
	public Double getCosts() {
		Double costs = 0.0;
		for (final Service s : services) {
			costs += (s.getPrice() - s.getDiscount()) * s.getQuantity();
		}
		return costs;
	}
}
