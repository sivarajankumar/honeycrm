package honeycrm.client.mvp.presenters;

import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.PluginRequest;
import honeycrm.client.misc.PluginResponse;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PluginPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		void setPresenter(PluginPresenter presenter);
		void setPlugins(PluginDescription[] result);
		void setResponse(PluginResponse result);
	}

	private honeycrm.client.mvp.presenters.PluginPresenter.Display view;
	private PluginServiceAsync pluginService;
	
	public PluginPresenter(final Display view, final ReadServiceAsync readService, final PluginServiceAsync pluginService) {
		this.view = view;
		this.pluginService = pluginService;
		
		view.setPresenter(this);
		
		pluginService.getPluginDescriptions(new AsyncCallback<PluginDescription[]>() {
			@Override
			public void onSuccess(PluginDescription[] result) {
				view.setPlugins(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
		pluginService.request(new PluginRequest(new PluginDescription("foo", ""), "getSomething"), new AsyncCallback<PluginResponse>() {
			@Override
			public void onSuccess(PluginResponse result) {
				view.setResponse(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		// TODO at the moment, this only makes sense for the UI-only plugins.
/*		GWT.runAsync(PluginPresenter.class, new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				pluginService.getAvailablePlugins(new AsyncCallback<AbstractPlugin[]>() {
					@Override
					public void onSuccess(AbstractPlugin[] result) {
						//final Platform platform = new Platform(header, readService);
								
						for (final AbstractPlugin plugin: result) {
							plugin.initialize(); //platform//, new HaveABreakGadgetView());
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
		});*/
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public void onSubmitComplete() {
		pluginService.getPluginDescriptions(new AsyncCallback<PluginDescription[]>() {
			@Override
			public void onSuccess(PluginDescription[] result) {
				view.setPlugins(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
		pluginService.request(new PluginRequest(new PluginDescription("foo", ""), "getSomething"), new AsyncCallback<PluginResponse>() {
			@Override
			public void onSuccess(PluginResponse result) {
				view.setResponse(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
}
