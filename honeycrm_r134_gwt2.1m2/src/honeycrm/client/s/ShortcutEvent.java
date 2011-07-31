package honeycrm.client.s;

import com.google.gwt.event.shared.GwtEvent;

public class ShortcutEvent extends GwtEvent<ShortcutEventHandler> {
	public static Type<ShortcutEventHandler> TYPE = new Type<ShortcutEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ShortcutEventHandler> getAssociatedType() {
		return TYPE;
	}

	private char code;
	
	public ShortcutEvent(char code) {
		this.code = code;
	}
	
	@Override
	protected void dispatch(ShortcutEventHandler handler) {
		handler.onShortcut(this);
	}
	
	public char getCode() {
		return code;
	}
}
