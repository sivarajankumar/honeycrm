package honeycrm.client.mvp.presenters;

import java.util.HashMap;
import java.util.HashSet;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.presenters.DashboardPresenter.Display;
import honeycrm.client.mvp.views.HasSelectionHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;

public class DashboardPresenter extends Selector {
	public interface Display extends HasSelectionHandler {
		void insertRefreshedData(ListQueryResult value);
		HasClickHandlers getAddButton();
		String getModule();
	}

	private Display view;
	private SimpleEventBus eventBus;
	private String module;

	public DashboardPresenter(final SimpleEventBus eventBus, final Display view, final String module) {
		super(eventBus, view);
		this.view = view;
		this.eventBus = eventBus;
		this.module = module;
		bind();
	}

	private void bind() {
		view.getAddButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				add();
			}
		});
	}

	public void add() {
		eventBus.fireEvent(new CreateEvent(module));
	}	
}
