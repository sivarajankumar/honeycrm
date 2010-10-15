package platform.client.foo;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import platform.client.AbstractPlugin;

public class FooPlugin extends AbstractPlugin {
	@Override
	public Widget getWidget() {
		final Label label = new Label("I am the great widget!");
		return label;
	}

	@Override
	public String getName() {
		return this.getClass().toString();
	}
}
