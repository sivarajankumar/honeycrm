package honeycrm.client.mvp.presenters;

import honeycrm.client.mvp.views.DetailView;
import honeycrm.client.mvp.views.ListView;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

public class ModulePresenter {
	public interface Display {
		Widget asWidget();
		ListView getList();
		DetailView getDetail();
		honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display getModuleButtonBar();
	}

	final Display view;
	final SimpleEventBus eventBus;
	final DetailPresenter detailPresenter;
	final ListPresenter listPresenter;

	public ModulePresenter(final String module, final Display view, final SimpleEventBus eventBus, final ReadServiceAsync readService, final UpdateServiceAsync updateService, final CreateServiceAsync createService) {
		this.view = view;
		this.eventBus = eventBus;
		this.detailPresenter = new DetailPresenter(eventBus, module, readService, updateService, createService, view.getDetail());
		this.listPresenter = new ListPresenter(readService, updateService, createService, view.getList(), eventBus, module);
		new ModuleButtonBarPresenter(view.getModuleButtonBar(), eventBus, module);
	}
}
