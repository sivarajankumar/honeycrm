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
	public String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	public double price;

	@FieldIntegerAnnotation(1)
	@Label("Qty")
	public int quantity;

	@FieldCurrencyAnnotation("0")
	@Label("Discount")
	public double discount;

	@FieldCurrencyAnnotation("0")
	@Label("Sum")
	public double sum;

	@FieldRelateAnnotation(Product.class)
	@Label("Product")
	public long productID;
}
