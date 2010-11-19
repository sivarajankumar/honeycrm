package honeycrm.client.mvp.events;

import com.google.gwt.event.shared.GwtEvent;

public class LocaleChangeEvent extends GwtEvent<LocaleChangeEventHandler> {
	public static Type<LocaleChangeEventHandler> TYPE = new Type<LocaleChangeEventHandler>();
	private final String locale;

	public LocaleChangeEvent(final String locale) {
		this.locale = locale;
	}

	@Override
	public Type<LocaleChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LocaleChangeEventHandler handler) {
		handler.onLocaleChangeEvent(this);
	}

	public String getLocale() {
		return locale;
	}
}
