package honeycrm.server.test.small.mocks;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.DashboardsPresenter.Display;
import honeycrm.client.mvp.views.DashboardView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class DashboardsViewMock implements Display {
	private HasClickHandlers refresh = new HasClickHandlersMock();
	private Collection<DashboardView> views = new ArrayList<DashboardView>();
	
	@Override
	public Collection<DashboardView> getDashboardViews() {
		return views;
	}

	@Override
	public HasClickHandlers getRefreshBtn() {
		return refresh;
	}

	@Override
	public void setDashboardModules(ArrayList<String> modules) {
	}

	@Override
	public void insertRefreshedData(HashMap<String, ListQueryResult> result) {
	}

}
