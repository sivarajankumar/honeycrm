package honeycrm.server.domain;

import honeycrm.client.field.FieldCurrency;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldString;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "price", "quantity", "discount", "sum" })
@DetailViewable( { "name", "price" })
@Quicksearchable( { "name" })
@Hidden
public class Service extends AbstractEntity {
	@Persistent
	private String name;
	@Persistent
	private double price;
	@Persistent
	private int quantity;
	@Persistent
	private double discount;
	@Persistent
	private double sum;

	public Service() {
		fields.add(new FieldString("name", "Name", "", 200));
		fields.add(new FieldCurrency("price", "Price", "0.0", 100));
		fields.add(new FieldInteger("quantity", "Qty", "1", 100));
		fields.add(new FieldCurrency("discount", "Discount", "0.0", 100));
		fields.add(new FieldCurrency("sum", "Sum", "0.0", 100, true));
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
}
