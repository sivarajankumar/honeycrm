package honeycrm.client.mvp.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;

public class PluginPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
	}

	private honeycrm.client.mvp.presenters.PluginPresenter.Display view;
	
	public PluginPresenter(final Display view, final ReadServiceAsync readService, final PluginServiceAsync pluginService) {
		this.view = view;
		
		GWT.runAsync(PluginPresenter.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				pluginService.getAvailablePlugins(new AsyncCallback<AbstractPlugin[]>() {
					@Override
					public void onSuccess(AbstractPlugin[] result) {
						//final Platform platform = new Platform(header, readService);
								
						for (final AbstractPlugin plugin: result) {
							plugin.initialize(); //platform/*, new HaveABreakGadgetView()*/);
							plugin.runPlugin();
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
			
			@Override
			public void onFailure(Throwable reason) {
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
}
