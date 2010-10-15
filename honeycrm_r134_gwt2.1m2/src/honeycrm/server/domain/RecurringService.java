package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Hidden;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldEnumAnnotation;

@ListViewable({ "productID", "price", "quantity", "unit2", "recurrence", "discount", "kindOfDiscount", "sum" })
@DetailViewable({ "name,productID", "discount,quantity", "kindOfDiscount", "price", "sum" })
@Quicksearchable({ "name" })
@Hidden
public class RecurringService extends DiscountableService {
	@Label("Unit")
	@FieldEnumAnnotation({ "hours", "pieces" })
	public String unit2;

	@Label("Recurrence")
	@FieldEnumAnnotation({ "monthly", "annually" })
	public String recurrence;
}
