package crm.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

// TODO when one operation finishes while a concurrent one is still running the loading is ended too early
public class LoadIndicator extends Composite {
	private FlowPanel panel = new FlowPanel();
	private Label loading = new Label("Loading.. ");

	private static final LoadIndicator instance = new LoadIndicator();

	private LoadIndicator() {
		panel.setStyleName("loadIndicator");
		panel.add(loading);

		loading.setVisible(false);
		initWidget(panel);
	}

	public static LoadIndicator get() {
		return instance;
	}

	public void startLoading() {
		loading.setVisible(true);
	}

	public void endLoading() {
		loading.setVisible(false);
	}
}
