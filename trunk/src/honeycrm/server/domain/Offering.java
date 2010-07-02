package honeycrm.server.domain;

import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldTable;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.RelatesTo;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "contactId", "deadline" })
@DetailViewable( { "contactId", "deadline", "services" })
@Quicksearchable( { "contactId", "deadline" })
public class Offering extends AbstractEntity {
	@Persistent
	private List<Service> services;
	@Persistent
	@RelatesTo(Contact.class)
	private Long contactId;
	@Persistent
	private Date deadline;

	public Offering() {
		fields.add(new FieldTable("services", "Services"));
		fields.add(new FieldRelate("contactId", "contact", "Contact"));
		fields.add(new FieldDate("deadline", "Deadline"));
	}

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
