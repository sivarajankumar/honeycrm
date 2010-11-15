package honeycrm.client.mvp.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import honeycrm.client.mvp.presenters.HeaderPresenter.Display;
import honeycrm.client.plugin.AbstractPlugin;
import honeycrm.client.plugin.Platform;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;

public class PluginPresenter {
	public PluginPresenter(final Display header, final ReadServiceAsync readService, final PluginServiceAsync pluginService) {
		GWT.runAsync(PluginPresenter.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				pluginService.getAvailablePlugins(new AsyncCallback<AbstractPlugin[]>() {
					@Override
					public void onSuccess(AbstractPlugin[] result) {
						final Platform platform = new Platform(header, readService);
								
						for (final AbstractPlugin plugin: result) {
							plugin.initialize(platform/*, new HaveABreakGadgetView()*/);
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

	
}
