package honeycrm.client.mvp.presenters;

import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.CreateEventHandler;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenEventHandler;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;

public class ModuleButtonBarPresenter {
	public interface Display {
		HasClickHandlers getSearchButton();
		void toggleVisibility(ModuleAction action);
	}

	private Display view;
	private SimpleEventBus eventBus;
	private String module;

	public ModuleButtonBarPresenter(final Display view, final SimpleEventBus eventBus, final String module) {
		this.view = view;
		this.module = module;
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		view.toggleVisibility(ModuleAction.INIT);
		
		eventBus.addHandler(CreateEvent.TYPE, new CreateEventHandler() {
			@Override
			public void onCreate(CreateEvent event) {
				if (module.equals(event.getModule())) {
					view.toggleVisibility(ModuleAction.CREATE);
				}
			}
		});
		eventBus.addHandler(OpenEvent.TYPE, new OpenEventHandler() {
			@Override
			public void onOpen(OpenEvent event) {
				if (module.equals(event.getDto().getModule())) {
					view.toggleVisibility(ModuleAction.DETAIL);
				}
			}
		});
	}
}
