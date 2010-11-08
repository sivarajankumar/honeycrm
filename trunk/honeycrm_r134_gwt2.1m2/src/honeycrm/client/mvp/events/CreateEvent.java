package honeycrm.client.mvp.events;

import java.util.HashMap;

import com.google.gwt.event.shared.GwtEvent;

public class CreateEvent extends GwtEvent<CreateEventHandler> {
	public static Type<CreateEventHandler> TYPE = new Type<CreateEventHandler>();
	private final String module;
	private final HashMap<String, Object> prefilledFields;

	public CreateEvent(String module) {
		this.module = module;
		this.prefilledFields = null;
	}
	
	public CreateEvent(String module, final HashMap<String, Object> prefilledFields) {
		this.module = module;
		this.prefilledFields = prefilledFields;
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
	
	public HashMap<String, Object> getPrefilledFields() {
		return prefilledFields;
	}
}
