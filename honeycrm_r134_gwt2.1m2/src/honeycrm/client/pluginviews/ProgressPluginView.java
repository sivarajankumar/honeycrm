package honeycrm.client.pluginviews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.views.ServiceTableView;
import honeycrm.client.plugin.ProgressPluginPresenter.Display;

public class ProgressPluginView extends Composite implements Display {
	private static ProgressPluginViewUiBinder uiBinder = GWT.create(ProgressPluginViewUiBinder.class);

	interface ProgressPluginViewUiBinder extends UiBinder<Widget, ProgressPluginView> {
	}

	@UiField Label progress;

	public ProgressPluginView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void display(final String progressSymbol) {
		progress.setText(progressSymbol);
	}
}
