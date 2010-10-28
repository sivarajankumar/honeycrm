package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface CreateEventHandler extends EventHandler {
	void onCreate(CreateEvent event);
}
