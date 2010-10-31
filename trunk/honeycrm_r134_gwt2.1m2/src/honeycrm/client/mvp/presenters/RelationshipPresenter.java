package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.views.HasSelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;

public class RelationshipPresenter extends Selector {
	public interface Display extends HasSelectionHandler {
		void setId(Long relatedId);
		void insertRefreshedData(ListQueryResult relationshipData);
	}
	
	public RelationshipPresenter(final SimpleEventBus eventBus, final Display view) {
		super(eventBus, view);
	}
}
