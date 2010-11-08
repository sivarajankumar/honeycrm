package honeycrm.client.mvp.presenters;

import java.util.HashMap;
import java.util.HashSet;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.views.HasSelectionHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;

public class RelationshipPresenter extends Selector {
	public interface Display extends HasSelectionHandler {
		void setId(Long relatedId);
		void insertRefreshedData(ListQueryResult relationshipData, long relatedId);
		String getRelatedModule();
		String getOriginatingModule();
		HasClickHandlers getAddButton();
		long getId();
	}

	private final Display view;
	private final SimpleEventBus eventBus;

	public RelationshipPresenter(final SimpleEventBus eventBus, final Display view) {
		super(eventBus, view);
		this.view = view;
		this.eventBus = eventBus;
		
		bind();
	}

	private void bind() {
		view.getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final HashMap<String, HashMap<String, HashSet<String>>> relationships = DtoModuleRegistry.instance().getRelationships();
				final String field = relationships.get(view.getOriginatingModule()).get(view.getRelatedModule()).iterator().next();
				final long id = view.getId();

				final HashMap<String, Object> prefilledFields = new HashMap<String, Object>();
				prefilledFields.put(field, id);
				
				eventBus.fireEvent(new CreateEvent(view.getOriginatingModule(), prefilledFields));
			}
		});
	}
}
