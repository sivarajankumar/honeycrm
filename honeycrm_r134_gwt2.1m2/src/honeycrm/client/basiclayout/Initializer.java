package honeycrm.client.basiclayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;

public class Initializer extends FlowPanel {
	/**
	 * We need to be online to load visualizations. Allow developers to disable loading to be able to work off-line.
	 */
	public static final boolean SKIP_LOADING_VISUALISATIONS = true;

	public Initializer() {
		//super(Unit.PX);
		initRealUserInterface();
	}
	
	private void initRealUserInterface() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				add(new Header());
				add(TabCenterView.instance());
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
	}
}
