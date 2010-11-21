package honeycrm.server.test.small.mocks;

import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;

import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.ContentPresenter.Display;

public class ContentViewMock implements Display {
	private honeycrm.client.mvp.presenters.DashboardsPresenter.Display dashboard = new DashboardsViewMock();
	private HasBeforeSelectionHandlers<Integer> panel = new HasBeforeSelectionHandlersMock<Integer>();
	private honeycrm.client.mvp.presenters.ModulePresenter.Display moduleView = new ModuleViewMock();

	@Override
	public honeycrm.client.mvp.presenters.DashboardsPresenter.Display getDashboard() {
		return dashboard;
	}

	@Override
	public honeycrm.client.mvp.presenters.ModulePresenter.Display getModuleViewByName(String moduleName) {
		return moduleView;
	}

	@Override
	public void setPresenter(ContentPresenter presenter) {

	}

	@Override
	public HasBeforeSelectionHandlers<Integer> getPanel() {
		return panel;
	}

	@Override
	public String getModuleAtPosition(Integer position) {
		return null;
	}

	@Override
	public void showModule(String module) {

	}

}
