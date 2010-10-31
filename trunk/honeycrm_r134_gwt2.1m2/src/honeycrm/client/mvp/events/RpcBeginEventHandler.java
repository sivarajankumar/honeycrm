package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface RpcBeginEventHandler extends EventHandler {
	void onRpcBegin(RpcBeginEvent event);
}
