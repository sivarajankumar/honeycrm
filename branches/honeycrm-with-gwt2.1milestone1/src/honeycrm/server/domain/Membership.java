package honeycrm.server.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
public class Membership extends AbstractEntity {
	@Persistent
	private long memberId; // relation to contacts
	@Persistent
	private long employeeId; // relation to employees
	@Persistent
	private double payment;
	@Persistent
	@SearchableProperty
	private String tiedToPurpose; // yes, no, 'soon'
	@Persistent
	@SearchableProperty
	private String purpose;
	@Persistent
	@SearchableProperty
	private String paymentMethod; // Direct Debit authorisation / transaction
	@Persistent
	private Date startDate;
	@Persistent
	private Date endDate;

	public Membership() {
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public String getTiedToPurpose() {
		return tiedToPurpose;
	}

	public void setTiedToPurpose(String tiedToPurpose) {
		this.tiedToPurpose = tiedToPurpose;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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
}
