package honeycrm.server.test.small.mocks;

import honeycrm.client.mvp.presenters.ModulePresenter.Display;

import com.google.gwt.user.client.ui.Widget;

public class ModuleViewMock implements Display {
	private honeycrm.client.mvp.presenters.DetailPresenter.Display detail = new DetailViewMock();
	private honeycrm.client.mvp.presenters.ListPresenter.Display list = new ListViewMock();
	private honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display bar = new ModuleButtonBarViewMock();

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public honeycrm.client.mvp.presenters.ListPresenter.Display getList() {
		return list;
	}

	@Override
	public honeycrm.client.mvp.presenters.DetailPresenter.Display getDetail() {
		return detail;
	}

	@Override
	public honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display getModuleButtonBar() {
		return bar ;
	}

}
