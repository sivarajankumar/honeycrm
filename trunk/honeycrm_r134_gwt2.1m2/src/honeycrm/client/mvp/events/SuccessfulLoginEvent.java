package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class SuccessfulLoginEvent extends GwtEvent<SuccessfulLoginEventHandler> {
	private String login;
	private long userId;
	public static Type<SuccessfulLoginEventHandler> TYPE = new Type<SuccessfulLoginEventHandler>();

	public SuccessfulLoginEvent(final String login, final long userId) {
		this.login = login;
		this.userId = userId;
	}

	@Override
	protected void dispatch(SuccessfulLoginEventHandler handler) {
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
