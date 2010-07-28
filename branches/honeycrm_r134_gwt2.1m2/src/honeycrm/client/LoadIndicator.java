package honeycrm.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class LoadIndicator extends Composite {
	/**
	 * Save how many actions have been triggered on the server by the client. The request to remove the load indicator will be fulfilled if all actions are finished.
	 */
	private long concurrentActions = 0;
	private Label loading = new Label("Loading.. ");

	private static final LoadIndicator instance = new LoadIndicator();

	private LoadIndicator() {
		loading.setStyleName("loadIndicator");
		loading.setVisible(false);
		initWidget(loading);
	}

	public static LoadIndicator get() {
		return instance;
	}

	public void startLoading() {
		loading.setVisible(true);
		++concurrentActions;
	}

	public void endLoading() {
		if (--concurrentActions == 0) {
			loading.setVisible(false);
		}
	}
}
