package honeycrm.server.domainNew;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;

import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable({"name", "accountId"})
@DetailViewable({"name", "accountId"})
@Quicksearchable("name")
class Contact extends AbstractEntity {
	@Label("Name")
	@FieldStringAnnotation
	public String name;
	
	@Label("Account")
	@FieldRelateAnnotation(Account.class)
	public Key accountId;
}
