package honeycrm.server.domain;

import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldString;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "price" })
@DetailViewable( { "name,price" })
@Quicksearchable( { "name" })
public class Product extends AbstractEntity {
	@Persistent
	private String name;
	@Persistent
	private double price;

	public Product() {
		fields.add(new FieldString("name", "Name"));
		fields.add(new FieldCurrency("price", "Price"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
