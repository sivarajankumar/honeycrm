package honeycrm.client.mvp.views;

import honeycrm.client.mvp.presenters.LoadPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoadView extends Composite implements Display {
	private static LoadViewUiBinder uiBinder = GWT.create(LoadViewUiBinder.class);

	interface LoadViewUiBinder extends UiBinder<Widget, LoadView> {
	}

	@UiField
	Label loadIndicator; 
	
	/**
	 * Save how many actions have been triggered on the server by the client. The request to remove the load indicator will be fulfilled if all actions are finished.
	 */
	@Deprecated
	private long concurrentActions = 0;
	@Deprecated
	private final Label loading = new Label("Loading.. ");

	private static final LoadView instance = new LoadView();

	public LoadView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasText getLoadIndicator() {
		return loadIndicator;
	}

	@Deprecated
	public static LoadView get() {
		return instance;
	}

	@Deprecated
	public void startLoading() {
		loading.setVisible(true);
		++concurrentActions;
	}

	@Deprecated
	public void endLoading() {
		if (--concurrentActions == 0) {
			loading.setVisible(false);
		} else if (concurrentActions < 0) {
			Window.alert("end loading too often called");
		}
	}

	@Override
	public void showLoadingIndicator(final boolean isVisible) {
		loadIndicator.setVisible(isVisible);
	}

	@Override
	public void onTooManyRpcEndEvents() {
		Window.alert("Too many RpcEndEvents occurred.");
	}
}
