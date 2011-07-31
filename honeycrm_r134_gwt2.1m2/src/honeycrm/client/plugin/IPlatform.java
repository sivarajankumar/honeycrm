package honeycrm.client.plugin;

import honeycrm.client.services.ReadServiceAsync;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

public interface IPlatform {
	ReadServiceAsync getReadService();
	void attachToHeader(final Widget w);
	void scheduleRepeating(Command command, int updateInterval);
}
