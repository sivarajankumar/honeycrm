package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "productID", "price", "quantity", "discount", "sum" })
@DetailViewable( { "name,productID", "discount,quantity", "price", "sum" })
@Quicksearchable( { "name" })
@Hidden
public class Service extends AbstractEntity {
	@FieldStringAnnotation
	@Label("Name")
	private String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	private double price;

	@FieldIntegerAnnotation(1)
	@Label("Qty")
	private int quantity;

	@FieldCurrencyAnnotation("0")
	@Label("Discount")
	private double discount;

	@FieldCurrencyAnnotation("0")
	@Label("Sum")
	private double sum;

	@FieldRelateAnnotation(Product.class)
	@Label("Product")
	private long productID;
	
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

	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}
}
