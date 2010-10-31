package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface RpcEndEventHandler extends EventHandler {
	void onRpcEnd(RpcEndEvent event);
}
