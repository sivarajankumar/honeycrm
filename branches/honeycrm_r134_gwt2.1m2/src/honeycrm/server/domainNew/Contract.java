package honeycrm.server.domainNew;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;

import java.util.ArrayList;
import java.util.Date;
import com.google.appengine.api.datastore.Key;

@Quicksearchable({ "contactId" })
@ListViewable({ "contactId", "startDate", "endDate" })
@DetailViewable({ "contactId", "startDate", "endDate" })
public class Contract extends AbstractEntity {
	@Label("Contact")
	@FieldRelateAnnotation(Contact.class)
	public Key contactId;

	@Label("Start Date")
	@FieldDateAnnotation
	public Date startDate;

	@Label("End Date")
	@FieldDateAnnotation
	public Date endDate;

	@Label("Services")
	@FieldTableAnnotation(UniqueService.class)
	@OneToMany(UniqueService.class)
	public ArrayList<Key> uniqueServices;
}
