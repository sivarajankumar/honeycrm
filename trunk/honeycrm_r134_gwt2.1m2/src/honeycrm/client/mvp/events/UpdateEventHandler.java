package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface UpdateEventHandler extends EventHandler {
	void onAddEvent(UpdateEvent event);
}
