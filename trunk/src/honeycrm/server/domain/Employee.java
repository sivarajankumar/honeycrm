package honeycrm.server.domain;

import honeycrm.client.field.FieldBoolean;
import honeycrm.client.field.FieldString;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable
@Searchable
@ListViewable( { "active", "name" })
@DetailViewable( { "name", "active" })
@Quicksearchable( { "name" })
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
