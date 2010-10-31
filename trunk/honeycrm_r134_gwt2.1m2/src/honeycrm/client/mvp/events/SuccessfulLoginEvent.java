package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class SuccessfulLoginEvent extends GwtEvent<SuccessfulLoginEventHandler> {
	private final String login;
	private final long userId;
	public static Type<SuccessfulLoginEventHandler> TYPE = new Type<SuccessfulLoginEventHandler>();

	public SuccessfulLoginEvent(final String login, final long userId) {
		this.login = login;
		this.userId = userId;
	}

	@Override
	protected void dispatch(final SuccessfulLoginEventHandler handler) {
		handler.onLogin(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SuccessfulLoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	public String getLogin() {
		return login;
	}
	
	public long getUserId() {
		return userId;
	}
}
