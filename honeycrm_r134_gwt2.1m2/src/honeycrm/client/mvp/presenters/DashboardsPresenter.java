package honeycrm.client.mvp.presenters;

import java.util.ArrayList;
import java.util.HashMap;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.mvp.events.UpdateEvent;
import honeycrm.client.mvp.events.UpdateEventHandler;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class DashboardsPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getRefreshBtn();
		void setDashboardModules(ArrayList<String> modules);
		void insertRefreshedData(HashMap<String, ListQueryResult> result);
	}

	final Display view;
	final ReadServiceAsync readService;
	final long userId;
	final SimpleEventBus eventBus;

	public DashboardsPresenter(final long userId, final Display view, final ReadServiceAsync readService, final SimpleEventBus eventBus) {
		this.userId = userId;
		this.view = view;
		this.readService = readService;
		this.eventBus = eventBus;

		final ArrayList<String> modules = new ArrayList<String>();
		for (final ModuleDto moduleDto: DtoModuleRegistry.instance().getDtos()) {
			if (moduleDto.getModule().contains("Service")) {
				continue;
			}
			modules.add(moduleDto.getModule());
		}
		view.setDashboardModules(modules);
		
		bind();
		
		refresh();
	}
	
	private void bind() {
		view.getRefreshBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		});
		
		eventBus.addHandler(UpdateEvent.TYPE, new UpdateEventHandler() {
			@Override
			public void onAddEvent(UpdateEvent event) {
				// Refresh dashboard automatically if user changed an item.
				// We do not remove the refresh button to allow the user to refresh to see the changes done by other users.
				refresh();
			}
		});
	}

	protected void refresh() {
		if (userId > 0) {
			readService.getAllAssignedTo(userId, 0, 20, new AsyncCallback<HashMap<String,ListQueryResult>>() {
				@Override
				public void onSuccess(HashMap<String, ListQueryResult> result) {
					view.insertRefreshedData(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}

	@Override
	public void go(HasWidgets container) {
	}
}
