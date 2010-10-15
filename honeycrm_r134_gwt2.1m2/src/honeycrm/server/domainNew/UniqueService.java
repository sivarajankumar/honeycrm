package honeycrm.server.domainNew;

import com.google.appengine.api.datastore.Key;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;

@ListViewable({ "productId", "price", "quantity", "sum" })
@DetailViewable({ "productId", "price", "quantity", "sum" })
public class UniqueService extends AbstractEntity {
	@Label("Product")
	@FieldRelateAnnotation(Product.class)
	public Key productId;

	@Label("Price")
	@FieldCurrencyAnnotation("0")
	public double price;

	@Label("Qty")
	@FieldIntegerAnnotation(1)
	public int quantity;

	@Label("Sum")
	@FieldCurrencyAnnotation("0")
	public double sum;
}
