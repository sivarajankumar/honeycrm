package platform.client.plugins;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import platform.client.AbstractPlugin;

public class FooPlugin extends AbstractPlugin {
	@Override
	public Widget getWidget() {
		final Label label = new Label("I am the " + getClass().toString() + " plugin!");
		return label;
	}
}
