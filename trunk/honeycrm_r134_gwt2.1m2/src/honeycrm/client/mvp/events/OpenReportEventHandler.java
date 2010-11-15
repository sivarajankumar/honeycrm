package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface OpenReportEventHandler extends EventHandler {
	void onOpenReport(OpenReportEvent openReportEvent);
}
