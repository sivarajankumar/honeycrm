package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateEvent extends GwtEvent<UpdateEventHandler> {
	public static Type<UpdateEventHandler> TYPE = new Type<UpdateEventHandler>();
	private final String module;

	public UpdateEvent(final String module) {
		this.module = module;
	}
	
	public String getModule() {
		return module;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final UpdateEventHandler handler) {
		handler.onAddEvent(this);
	}
}


