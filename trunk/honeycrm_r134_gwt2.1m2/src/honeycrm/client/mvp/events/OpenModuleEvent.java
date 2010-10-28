package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class OpenModuleEvent extends GwtEvent<OpenModuleEventHandler> {
	public static Type<OpenModuleEventHandler> TYPE = new Type<OpenModuleEventHandler>();
	private final String module;
	
	public OpenModuleEvent(final String module) {
		this.module = module;
	}

	@Override
	public Type<OpenModuleEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenModuleEventHandler handler) {
		handler.onOpenModule(this);
	}
	
	public String getModule() {
		return module;
	}
}
