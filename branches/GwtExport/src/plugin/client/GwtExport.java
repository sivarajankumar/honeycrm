package plugin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtExport implements EntryPoint {
	private native void loadNewPlugin(final String name) /*-{
		$wnd.plugin.PluginRegistry.registerPlugin(name);

		// should be this according to the documentation. but does not work.
		// plugin.client.PluginRegistry.registerPlugin("foo");
	}-*/;

	private native void onLoadImpl() /*-{
		//alert("now");
		if ($wnd.jscOnLoad && typeof $wnd.jscOnLoad == 'function') $wnd.jscOnLoad();
	}-*/;

	@Override
	public void onModuleLoad() {
		GWT.create(PluginRegistry.class);
		onLoadImpl();

		RootPanel.get().add(new PluginPresenter());

		new Timer() {
			@Override
			public void run() {
				loadNewPlugin("foo");

				new Timer() {
					@Override
					public void run() {
						loadNewPlugin("bar");

						new Timer() {
							@Override
							public void run() {
								loadNewPlugin("baz");
							}
						}.schedule(500);
					}
				}.schedule(500);
			}
		}.schedule(500);
	}
}
