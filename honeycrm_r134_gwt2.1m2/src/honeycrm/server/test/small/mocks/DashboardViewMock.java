package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.DashboardPresenter.Display;
import honeycrm.client.mvp.views.SelectionHandler;
import honeycrm.server.domain.Contact;

public class DashboardViewMock implements Display {
	private HasClickHandlers add = new HasClickHandlersMock();
	
	@Override
	public void setSelectionHandler(SelectionHandler handler) {
	}

	@Override
	public void insertRefreshedData(ListQueryResult value) {
	}

	@Override
	public HasClickHandlers getAddButton() {
		return add;
	}

	@Override
	public String getModule() {
		return Contact.class.getSimpleName();
	}
}
