package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class HasKeyDownHandlersMock implements HasKeyDownHandlers {
	@Override
	public void fireEvent(GwtEvent<?> event) {
		
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return null;
	}
}
