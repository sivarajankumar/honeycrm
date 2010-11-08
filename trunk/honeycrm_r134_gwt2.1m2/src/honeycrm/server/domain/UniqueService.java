package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;

@ListViewable({ "vat", "productID", "productCode", "price", "quantity", "unit", "discount", "kindOfDiscount", "sum" })
@DetailViewable({ "name,productID", "vat", "discount,quantity", "productCode", "kindOfDiscount", "price", "sum" })
@Quicksearchable({ "name" })
@Hidden
public class UniqueService extends DiscountableService {
	@Label("Unit")
	@FieldEnumAnnotation({ "pieces", "hours" })
	public String unit;
}
