package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class RpcBeginEvent extends GwtEvent<RpcBeginEventHandler> {
	public static Type<RpcBeginEventHandler> TYPE = new Type<RpcBeginEventHandler>();

	@Override
	public Type<RpcBeginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final RpcBeginEventHandler handler) {
		handler.onRpcBegin(this);
	}
}
