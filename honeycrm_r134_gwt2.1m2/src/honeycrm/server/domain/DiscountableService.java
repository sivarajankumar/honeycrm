package honeycrm.server.domain;

import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import com.google.appengine.api.datastore.Key;

@Hidden
public abstract class DiscountableService extends AbstractEntity {
	@FieldBooleanAnnotation(defaultValue=true)
	@Label("VAT")
	public boolean vat;
	
	@FieldStringAnnotation
	@Label("Name")
	public String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	public double price;

	@FieldIntegerAnnotation(1)
	@Label("Qty")
	public int quantity;
	
	@FieldStringAnnotation
	@Label("Product Code")
	public String productCode;

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
