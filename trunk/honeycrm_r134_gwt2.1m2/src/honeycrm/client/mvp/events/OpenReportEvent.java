package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class OpenReportEvent extends GwtEvent<OpenReportEventHandler> {
	public static Type<OpenReportEventHandler> TYPE = new Type<OpenReportEventHandler>();
	private final int reportId;

	public OpenReportEvent(final int id) {
		this.reportId = id;
	}

	public int getReportId() {
		return reportId;
	}

	@Override
	public Type<OpenReportEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenReportEventHandler handler) {
		handler.onOpenReport(this);
	}
}
