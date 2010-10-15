package platform.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

abstract public class AbstractPlugin implements EntryPoint {
	private native void loadNewPluginObject(final AbstractPlugin plugin) /*-{
		$wnd.platform.PluginRegistry.registerPluginObject(plugin);
	}-*/;

	@Override
	public void onModuleLoad() {
		final AbstractPlugin p = this;

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				new Timer() {
					@Override
					public void run() {
						loadNewPluginObject(p);
					}
				}.schedule(2 * 1000);
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not load code " + this.getClass().toString());
			}
		});
	}

	final public String getName() {
		return this.getClass().toString();
	}

	abstract protected Widget getWidget();
}
