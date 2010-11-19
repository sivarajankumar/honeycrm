package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class HasClickHandlersMock implements HasClickHandlers {
	@Override
	public void fireEvent(GwtEvent<?> event) {
		
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return null;
	}
}
