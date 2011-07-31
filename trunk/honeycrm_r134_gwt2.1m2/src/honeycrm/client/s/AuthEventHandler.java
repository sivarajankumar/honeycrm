package honeycrm.client.s;

import com.google.gwt.event.shared.EventHandler;

public interface AuthEventHandler extends EventHandler {
	void onAuth(AuthEvent event);
}