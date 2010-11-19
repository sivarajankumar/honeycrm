package honeycrm.server.test.small.mocks;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class HasStringValueMock implements HasValue<String> {
	private String value = "";
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return null;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
	}
}
