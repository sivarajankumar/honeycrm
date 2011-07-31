package honeycrm.server.test.small.mocks;

import honeycrm.client.mvp.presenters.HeaderPresenter.Display;

import com.google.gwt.user.client.ui.Widget;

public class HeaderViewMock implements Display {
	private honeycrm.client.mvp.presenters.LoadPresenter.Display loadView;

	public HeaderViewMock(final honeycrm.client.mvp.presenters.LoadPresenter.Display loadView) {
		this.loadView = loadView;
	}

	@Override
	public honeycrm.client.mvp.presenters.LoadPresenter.Display getLoadView() {
		return loadView;
	}

	@Override
	public void attachPluginWidget(Widget w) {
		System.out.println("attached plugin");
	}
}
