package honeycrm.client.basiclayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;

// TODO bundle all startup specific requests into one single request
public class Initializer extends DockLayoutPanel {
	/**
	 * We need to be online to load visualizations. Allow developers to disable loading to be able to work off-line.
	 */
	public static final boolean SKIP_LOADING_VISUALISATIONS = false;

	public Initializer() {
		super(Unit.PX);
		initRealUserInterface();
	}
	
	private void initRealUserInterface() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				addNorth(new Header(), 40);
				add(TabCenterView.instance());
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
	}
}
