package honeycrm.server.domain;

import honeycrm.client.dto.DetailViewable;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListViewable;
import honeycrm.client.dto.RelatesTo;
import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldEnum;
import honeycrm.client.field.FieldRelate;
import honeycrm.client.field.FieldText;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable({"marked", "memberId", "employeeId", "startDate"})
@DetailViewable({"memberId,employeeId", "tiedToPurpose,purpose", "payment,paymentMethod", "startDate,endDate"})
public class Membership extends AbstractEntity {
	@Persistent
	@RelatesTo(Contact.class)
	private long memberId; // relation to contacts
	@Persistent
	@RelatesTo(Employee.class)
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

	static {
		fields.add(new FieldRelate("memberId", new Dto(), "Member"));
		fields.add(new FieldRelate("employeeId", new Dto(), "Employee"));
		fields.add(new FieldCurrency("payment", "Contribution"));
		fields.add(new FieldEnum("tiedToPurpose", "Tied to purpose", "Yes", "No", "Soon"));
		fields.add(new FieldText("purpose", "Purpose"));
		fields.add(new FieldEnum("paymentMethod", "Payment method", "Direct Debit authorisation", "transaction"));
		fields.add(new FieldDate("startDate", "Start date"));
		fields.add(new FieldDate("endDate", "End date"));
	}
	
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
