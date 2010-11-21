package honeycrm.server.test.small.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;

import honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display;
import honeycrm.client.view.ModuleAction;

public class ModuleButtonBarViewMock implements Display {
	private HasClickHandlers search = new HasClickHandlersMock();

	@Override
	public HasClickHandlers getSearchButton() {
		return search;
	}

	@Override
	public void toggleVisibility(ModuleAction action) {
	}
}
