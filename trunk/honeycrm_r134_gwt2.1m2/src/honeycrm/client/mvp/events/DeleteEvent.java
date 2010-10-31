package honeycrm.client.mvp.events;

import java.util.HashSet;

import com.google.gwt.event.shared.GwtEvent;

public class DeleteEvent extends GwtEvent<DeleteEventHandler> {
	public static Type<DeleteEventHandler> TYPE = new Type<DeleteEventHandler>();
	
	final String kind;
	final HashSet<Long> deleteIds;

	public DeleteEvent(final String kind, final HashSet<Long> deleteIds) {
		this.deleteIds = deleteIds;
		this.kind = kind;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DeleteEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final DeleteEventHandler handler) {
		handler.onDeleteEvent(this);
	}
	
	public HashSet<Long> getDeleteIds() {
		return deleteIds;
	}
	
	public String getKind() {
		return kind;
	}
}
