package platform.client.plugins;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import platform.client.AbstractPlugin;

public class ClockPlugin extends AbstractPlugin {
	@Override
	protected Widget getWidget() {
		final Label l = new Label();
		
		new Timer() {
			@Override
			public void run() {
				l.setText(new Date(System.currentTimeMillis()).toString());
			}
		}.scheduleRepeating(1000);
		
		return l;
	}
}
