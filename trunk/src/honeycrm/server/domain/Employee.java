package honeycrm.server.domain;

import honeycrm.client.dto.DetailViewable;
import honeycrm.client.dto.ListViewable;
import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldString;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "active", "name" })
@DetailViewable( { "name", "active" })
public class Employee extends AbstractEntity {
	@Persistent
	@SearchableProperty
	private String name;
	@Persistent
	private boolean active;

	public Employee() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldBoolean("active", "Active"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
