package honeycrm.client.mvp.presenters;

import java.util.Map;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenEventHandler;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class RelationshipPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		void setPresenter(RelationshipPresenter presenter);
		void refresh(Long relatedId);
		void makeVisible();
		void insertRefreshedData(Map<String, ListQueryResult> result);
	}

	final String module;
	final Display view;
	final ReadServiceAsync readService;
	final SimpleEventBus eventBus;
	
	public RelationshipPresenter(final String module, final Display view, final ReadServiceAsync readService, final SimpleEventBus eventBus) {
		this.module = module;
		this.view = view;
		this.readService = readService;
		this.eventBus = eventBus;
		
		bind();
	}

	private void bind() {
		view.setPresenter(this);

		eventBus.addHandler(OpenEvent.TYPE, new OpenEventHandler() {
			@Override
			public void onOpen(OpenEvent event) {
				refreshRelationships(event);
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	private void refreshRelationships(OpenEvent event) {
		if (module.equals(event.getDto().getModule())) {
			view.makeVisible();
			// view.refresh(event.getDto().getId());
			
			readService.getAllRelated(event.getDto().getId(), event.getDto().getModule(), new AsyncCallback<Map<String,ListQueryResult>>() {
				@Override
				public void onSuccess(Map<String, ListQueryResult> result) {
					view.insertRefreshedData(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
				}
			});
		}
	}

}
