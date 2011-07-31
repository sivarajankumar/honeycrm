package honeycrm.server.test.small.mocks;

import honeycrm.client.mvp.presenters.ListPresenter.Display;
import honeycrm.client.mvp.views.SelectionHandler;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

public class ListViewMock implements Display {
	private HasClickHandlers add = new HasClickHandlersMock();
	private HasClickHandlers delete = new HasClickHandlersMock();

	@Override
	public void setSelectionHandler(SelectionHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldDelete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HasClickHandlers getAddButton() {
		return add;
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		return delete;
	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
