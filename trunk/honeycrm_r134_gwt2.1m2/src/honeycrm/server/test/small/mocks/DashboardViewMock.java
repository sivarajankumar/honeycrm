package honeycrm.server.test.small.mocks;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.DashboardPresenter.Display;
import honeycrm.client.mvp.views.SelectionHandler;

public class DashboardViewMock implements Display {
	@Override
	public void setSelectionHandler(SelectionHandler handler) {
	}

	@Override
	public void insertRefreshedData(ListQueryResult value) {
	}
}
