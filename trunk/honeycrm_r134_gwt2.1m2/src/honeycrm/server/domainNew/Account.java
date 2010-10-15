package honeycrm.server.domainNew;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

@SearchableEntity
@ListViewable({ "name", "annualRevenue" })
@DetailViewable({ "name", "annualRevenue" })
@Quicksearchable("name")
class Account extends AbstractEntity {
	@Label("Name")
	@FieldStringAnnotation
	public String name;
	
	@Label("Annual Revenue")
	@FieldCurrencyAnnotation("0")
	public double annualRevenue;
}
