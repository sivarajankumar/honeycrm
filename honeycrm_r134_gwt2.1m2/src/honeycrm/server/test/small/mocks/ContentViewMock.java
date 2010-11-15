package honeycrm.server.test.small.mocks;

import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;

import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.ContentPresenter.Display;

public class ContentViewMock implements Display {

	@Override
	public honeycrm.client.mvp.presenters.DashboardsPresenter.Display getDashboard() {
		return null;
	}

	@Override
	public honeycrm.client.mvp.presenters.ModulePresenter.Display getModuleViewByName(String moduleName) {
		return null;
	}

	@Override
	public void setPresenter(ContentPresenter presenter) {
		
	}

	@Override
	public HasBeforeSelectionHandlers<Integer> getPanel() {
		return null;
	}

	@Override
	public String getModuleAtPosition(Integer position) {
		return null;
	}

	@Override
	public void showModule(String module) {
		
	}

}
