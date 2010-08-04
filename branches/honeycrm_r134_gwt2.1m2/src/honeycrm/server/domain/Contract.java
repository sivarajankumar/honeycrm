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
@DetailViewable({ "contactID", "employeeID", "startDate", "endDate", "services" })
@ListViewable({ "contactID", "employeeID", "endDate" })
@Quicksearchable({"contactID"})
public class Contract extends AbstractEntity {
	@Label("Offering")
	@FieldRelateAnnotation(Offering.class)
	private Long offeringID;

	@Label("Employee")
	@FieldRelateAnnotation(Employee.class)
	private Long employeeID;

	@Label("Contact")
	@FieldRelateAnnotation(Contact.class)
	private Long contactID;

	@Label("Start Date")
	@FieldDateAnnotation
	private Date startDate;

	@Label("End Date")
	@FieldDateAnnotation
	private Date endDate;

	@Label("Services")
	@FieldTableAnnotation(Service.class)
	private List<Service> services;

	public Long getOfferingID() {
		return offeringID;
	}

	public void setOfferingID(Long offeringID) {
		this.offeringID = offeringID;
	}

	public Long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Long employeeID) {
		this.employeeID = employeeID;
	}

	public Long getContactID() {
		return contactID;
	}

	public void setContactID(Long contactID) {
		this.contactID = contactID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
