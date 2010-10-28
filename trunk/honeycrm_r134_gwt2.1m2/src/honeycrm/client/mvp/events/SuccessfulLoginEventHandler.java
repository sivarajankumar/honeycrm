package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface SuccessfulLoginEventHandler extends EventHandler {
	void onLogin(SuccessfulLoginEvent event);
}
