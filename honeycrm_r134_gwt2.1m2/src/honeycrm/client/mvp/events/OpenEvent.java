package honeycrm.client.mvp.events;

import honeycrm.client.dto.Dto;

import com.google.gwt.event.shared.GwtEvent;

public class OpenEvent extends GwtEvent<OpenEventHandler> {
	public static Type<OpenEventHandler> TYPE = new Type<OpenEventHandler>();
	private final Dto dto;

	public OpenEvent(final Dto dto) {
		this.dto = dto;
	}

	@Override
	public Type<OpenEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final OpenEventHandler handler) {
		handler.onOpen(this);
	}
	
	public Dto getDto() {
		return dto;
	}
}
