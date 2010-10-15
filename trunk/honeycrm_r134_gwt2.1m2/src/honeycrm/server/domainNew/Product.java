package honeycrm.server.domainNew;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@Quicksearchable({ "name" })
@DetailViewable({ "name", "price", "predecessor", "description" })
@ListViewable({ "name", "price", "predecessor", "description" })
public class Product extends AbstractEntity {
	@Label("Name")
	@FieldStringAnnotation
	public String name;

	@Label("Price")
	@FieldCurrencyAnnotation("0")
	public double price;

	@Label("Description")
	@FieldTextAnnotation(width = 400)
	public String description;

	@Label("Predecessor")
	@FieldRelateAnnotation(Product.class)
	public Key predecessor;
}
