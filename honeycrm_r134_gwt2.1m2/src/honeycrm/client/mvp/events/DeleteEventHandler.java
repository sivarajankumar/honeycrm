package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface DeleteEventHandler extends EventHandler {
	void onDeleteEvent(DeleteEvent event);
}
