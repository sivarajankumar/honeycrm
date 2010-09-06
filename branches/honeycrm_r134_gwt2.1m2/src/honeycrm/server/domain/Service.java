package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.compass.annotations.Searchable;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Searchable
@ListViewable( { "productID", "price", "quantity", "discount", "kindOfDiscount", "sum" })
@DetailViewable( { "name,productID", "discount,quantity", "kindOfDiscount", "price", "sum" })
@Quicksearchable( { "name" })
@Hidden
// @Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
// TODO perhaps service/offerings/contracts bug is caused by appengine/datastore bug: storage of owned relationships when children and parent are of same class
public class Service implements Bean {
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	public Key id;

	@FieldStringAnnotation
	@Label("Name")
	public String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	public double price;

	@FieldIntegerAnnotation(1)
	@Label("Qty")
	public int quantity;

	@FieldEnumAnnotation({"%", "abs"})
	@Label("Kind")
	public String kindOfDiscount;
	
	@FieldIntegerAnnotation(0)
	@Label("Discount")
	public int discount;

	@FieldCurrencyAnnotation("0")
	@Label("Sum")
	public double sum;

	@FieldRelateAnnotation(Product.class)
	@Label("Product")
	public long productID;

	@Override
	public void setId(Key id) {
		this.id = id;
	}

	@Override
	public Key getId() {
		return id;
	}
}
