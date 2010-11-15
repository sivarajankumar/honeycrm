package honeycrm.client.pluginviews;

import honeycrm.client.plugin.HaveABreakGadgetPresenter.Display;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;

public class HaveABreakGadgetView extends Composite implements Display {
	private final Label label = new Label();
	
	public HaveABreakGadgetView() {
		initWidget(label);
	}
	
	@Override
	public HasText getLabel() {
		return label;
	}
}
