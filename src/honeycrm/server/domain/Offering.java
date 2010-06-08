package honeycrm.server.domain;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
public class Offering extends AbstractEntity {
	@Persistent
	private List<Service> services;
	@Persistent
	private Long contactId;
	@Persistent
	private Date deadline;

	public Offering() {
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
}
