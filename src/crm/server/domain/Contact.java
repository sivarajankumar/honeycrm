package crm.server.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Contact {
	@Persistent
	private String city;
	@Persistent
	private String email;
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Key id;
	@Persistent
	private String name;
	@Persistent
	private String phone;
	@Persistent
	private long accountID;

	public Contact() {
	}

	public Contact(final String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public String getEmail() {
		return email;
	}

	public Key getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}
}
