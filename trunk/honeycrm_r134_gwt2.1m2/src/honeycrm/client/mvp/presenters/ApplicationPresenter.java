package honeycrm.client.mvp.presenters;

import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationPresenter implements Presenter {
	public interface Display {
		ContentPresenter.Display getContentView();
		honeycrm.client.mvp.presenters.HeaderPresenter.Display getHeader();
		Widget asWidget();
	}
	
	private final Display view;
	
	public ApplicationPresenter(long userId, ReadServiceAsync readService, final CreateServiceAsync createService, final UpdateServiceAsync updateService, final PluginServiceAsync pluginService, SimpleEventBus eventBus, Display applicationView) {
		this.view = applicationView;
		
		new LoadPresenter(view.getHeader().getLoadView(), eventBus);
		new ContentPresenter(userId, view.getContentView(), eventBus, readService, updateService, createService);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
