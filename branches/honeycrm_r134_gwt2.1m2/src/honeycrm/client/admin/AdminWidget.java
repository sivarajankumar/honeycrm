package honeycrm.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminWidget extends Composite {
	public AdminWidget() {
		final Panel table = new VerticalPanel();
		initWidget(new ScrollPanel(table));

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				for (final Composite widget : new Composite[] { new LocaleSettingsWidget(), new DatabaseWidget(), new CacheStatsWidget(), LogConsole.get() }) {
					table.add(widget);
				}
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
	}
}
