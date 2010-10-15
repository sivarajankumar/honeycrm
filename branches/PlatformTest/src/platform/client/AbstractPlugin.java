package platform.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

abstract public class AbstractPlugin implements EntryPoint {
	private native void loadNewPlugin(final String name) /*-{
		$wnd.platform.PluginRegistry.registerPlugin(name);
	}-*/;

	@Override
	public void onModuleLoad() {
		new Timer() {
			@Override
			public void run() {
				loadNewPlugin(getName());
			}
		}.schedule(2 * 1000);
	}

	abstract protected String getName();

	abstract protected Widget getWidget();
}
