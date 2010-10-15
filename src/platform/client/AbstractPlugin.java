package platform.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

abstract public class AbstractPlugin implements EntryPoint {
	private native void loadNewPluginObject(final AbstractPlugin plugin) /*-{
		$wnd.platform.PluginRegistry.registerPluginObject(plugin);
	}-*/;
	
	@Override
	public void onModuleLoad() {
		final AbstractPlugin p = this;
		new Timer() {
			@Override
			public void run() {
				loadNewPluginObject(p);
			}
		}.schedule(2 * 1000);
	}

	final public String getName() {
		return this.getClass().toString();
	}

	abstract protected Widget getWidget();
}
