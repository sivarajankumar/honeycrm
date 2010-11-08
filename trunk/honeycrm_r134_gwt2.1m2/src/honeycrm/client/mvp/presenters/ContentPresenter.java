package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.CreateEventHandler;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.events.OpenEventHandler;
import honeycrm.client.mvp.events.OpenModuleEvent;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;

public class ContentPresenter implements Presenter {
	public interface Display {
		DashboardsPresenter.Display getDashboard();
		ModulePresenter.Display getModuleViewByName(String moduleName);
		void setPresenter(ContentPresenter presenter);
		HasBeforeSelectionHandlers<Integer> getPanel();
		String getModuleAtPosition(Integer position);
		void showModule(String module);
	}

	final SimpleEventBus eventBus;
	final Display view;

	public ContentPresenter(final long userId, final Display view, final SimpleEventBus eventBus, final ReadServiceAsync readService, final UpdateServiceAsync updateService, final CreateServiceAsync createService) {
		this.eventBus = eventBus;
		this.view = view;
		
		for (final ModuleDto module : DtoModuleRegistry.instance().getDtos()) {
			if (!module.isHidden()) {
				// Create a presenter for each ModuleView instance
				// This sets up the required communication between view and presenters.
				final ModulePresenter.Display moduleView = view.getModuleViewByName(module.getModule());
				new ModulePresenter(module.getModule(), moduleView, eventBus, readService, updateService, createService);
			}
		}
		
		// Create a DashboardPresenter that takes care of the dashboard.
		new DashboardsPresenter(userId, view.getDashboard(), readService, eventBus);

		bind();
	}

	private void bind() {
		view.setPresenter(this);
		
		eventBus.addHandler(OpenEvent.TYPE, new OpenEventHandler() {
			@Override
			public void onOpen(OpenEvent event) {
				view.showModule(event.getDto().getModule());
			}
		});
		eventBus.addHandler(CreateEvent.TYPE, new CreateEventHandler() {
			@Override
			public void onCreate(CreateEvent event) {
				// Make sure the module tab is opened, too.
				view.showModule(event.getModule());
			}
		});
		view.getPanel().addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				eventBus.fireEvent(new OpenModuleEvent(view.getModuleAtPosition(event.getItem())));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
	}

	public void onCreate(final String module) {
		eventBus.fireEvent(new CreateEvent(module));
	}
}
