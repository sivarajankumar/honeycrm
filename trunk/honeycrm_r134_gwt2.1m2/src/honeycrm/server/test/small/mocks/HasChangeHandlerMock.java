package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class HasChangeHandlerMock implements HasChangeHandlers {
	@Override
	public void fireEvent(GwtEvent<?> event) {
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return null;
	}
}
