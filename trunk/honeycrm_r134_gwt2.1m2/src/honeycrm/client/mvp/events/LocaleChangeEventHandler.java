package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.EventHandler;

public interface LocaleChangeEventHandler extends EventHandler {
	void onLocaleChangeEvent(LocaleChangeEvent event);
}
