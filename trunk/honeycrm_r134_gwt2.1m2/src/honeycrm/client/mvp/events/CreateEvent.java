package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class CreateEvent extends GwtEvent<CreateEventHandler> {
	public static Type<CreateEventHandler> TYPE = new Type<CreateEventHandler>();
	private final String module;

	public CreateEvent(String module) {
		this.module = module;
	}

	@Override
	public Type<CreateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateEventHandler handler) {
		handler.onCreate(this);
	}
	
	public String getModule() {
		return module;
	}
}
