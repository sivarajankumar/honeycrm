package honeycrm.server.domain;

import com.google.appengine.api.datastore.Key;

import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

@Hidden
public abstract class DiscountableService extends AbstractEntity {
	@FieldStringAnnotation
	@Label("Name")
	public String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	public double price;

	@FieldIntegerAnnotation(1)
	@Label("Qty")
	public int quantity;

	@FieldEnumAnnotation({ "%", "abs" })
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
	public Key productID;
}
