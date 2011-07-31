package honeycrm.client.s;

import honeycrm.client.mvp.events.LocaleChangeEvent;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.LocaleInfo;

public class AuthPresenter extends AbstractPresenter {
	public interface Display extends AbstractPresenterDisplay {
		String getUsername();

		String getPassword();

		HasClickHandlers getLoginBtn();

		HasChangeHandlers getLanguage();

		String getCurrentLocale();

		HasKeyDownHandlers getUsernameField();

		HasKeyDownHandlers getPasswordField();

		void setCurrentLocale(LocaleInfo currentLocale);
	}

	public AuthPresenter(final SimpleEventBus bus, final Display view) {
		this.view = view;
		view.setCurrentLocale(LocaleInfo.getCurrentLocale());
		view.getUsernameField().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					bus.fireEvent(new AuthEvent(view.getUsername(), view.getPassword()));
				}
			}
		});
		view.getPasswordField().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					bus.fireEvent(new AuthEvent(view.getUsername(), view.getPassword()));
				}
			}
		});
		view.getLoginBtn().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bus.fireEvent(new AuthEvent(view.getUsername(), view.getPassword()));
			}
		});
		view.getLanguage().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				bus.fireEvent(new LocaleChangeEvent(view.getCurrentLocale()));
			}
		});
	}
}
