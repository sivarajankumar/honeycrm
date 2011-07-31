package honeycrm.client.s;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class ContactsPresenter extends AbstractPresenter {
	public interface Display extends AbstractPresenterDisplay {
		HasClickHandlers getCreate();

		void showCreate();
	}

	public ContactsPresenter(final Display view) {
		this.view = view;
		view.getCreate().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.showCreate();
			}
		});
	}
}
