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

	public LoadView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasText getLoadIndicator() {
		return loadIndicator;
	}

	@Override
	public void showLoadingIndicator(final boolean isVisible) {
		loadIndicator.setVisible(isVisible);
	}
}
