package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface OpenEventHandler extends EventHandler {
	void onOpen(OpenEvent event);
}
