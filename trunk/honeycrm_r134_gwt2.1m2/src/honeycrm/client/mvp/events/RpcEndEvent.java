package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class RpcEndEvent extends GwtEvent<RpcEndEventHandler> {
	public static Type<RpcEndEventHandler> TYPE = new Type<RpcEndEventHandler>();

	@Override
	public Type<RpcEndEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RpcEndEventHandler handler) {
		handler.onRpcEnd(this);
	}
}
