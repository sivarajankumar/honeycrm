package honeycrm.client.s;

import com.google.gwt.event.shared.GwtEvent;

public class AuthEvent extends GwtEvent<AuthEventHandler> {
	public static Type<AuthEventHandler> TYPE = new Type<AuthEventHandler>();

	private String username;
	private String password;
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AuthEventHandler> getAssociatedType() {
		return TYPE;
	}
	
	public AuthEvent(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch(AuthEventHandler handler) {
		handler.onAuth(this);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
