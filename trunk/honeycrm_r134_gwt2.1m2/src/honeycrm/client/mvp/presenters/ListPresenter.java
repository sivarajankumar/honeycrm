package honeycrm.client.mvp.presenters;

import java.util.HashSet;

import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.DeleteEvent;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenModuleEvent;
import honeycrm.client.mvp.events.OpenModuleEventHandler;
import honeycrm.client.mvp.events.UpdateEvent;
import honeycrm.client.mvp.events.UpdateEventHandler;
import honeycrm.client.mvp.views.HasSelectionHandler;
import honeycrm.client.mvp.views.SelectionHandler;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

public class ListPresenter extends Selector {
	public interface Display extends HasSelectionHandler {
		boolean shouldDelete();
		HasClickHandlers getAddButton();
		HasClickHandlers getDeleteButton();
		Widget asWidget();
		void refresh();
	}

	boolean listViewInitialized = false;
	final String module;
	final SimpleEventBus eventBus;
	final Display view;
	final ReadServiceAsync readService;
	final UpdateServiceAsync updateService;
	final CreateServiceAsync createService;

	public ListPresenter(final ReadServiceAsync readService, final UpdateServiceAsync updateService, final CreateServiceAsync createService, final Display view, final SimpleEventBus eventBus, final String kind) {
		super(eventBus, view);
		
		this.view = view;
		this.readService = readService;
		this.updateService = updateService;
		this.createService = createService;
		this.eventBus = eventBus;
		this.module = kind;
		
		bind();
	}

	private void bind() {
		view.setSelectionHandler(new SelectionHandler() {
			@Override
			public void onSelect(Dto dto) {
				eventBus.fireEvent(new OpenEvent(dto));				
			}
		});
		
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (view.shouldDelete()) {
					eventBus.fireEvent(new DeleteEvent(module, new HashSet<Long>()));
				}
			}
		});
		
		eventBus.addHandler(UpdateEvent.TYPE, new UpdateEventHandler() {
			@Override
			public void onAddEvent(UpdateEvent event) {
				refresh(event.getModule());
			}
		});
		eventBus.addHandler(OpenModuleEvent.TYPE, new OpenModuleEventHandler() {
			@Override
			public void onOpenModule(OpenModuleEvent event) {
				refresh(event.getModule());
			}
		});
	}

	public void refresh(final String refreshModule) {
		if (module.equals(refreshModule)) {
			view.refresh();
		}
	}
}
