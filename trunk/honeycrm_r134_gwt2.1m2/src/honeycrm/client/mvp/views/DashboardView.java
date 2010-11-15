package honeycrm.client.mvp.views;

import com.google.gwt.view.client.HasData;

import honeycrm.client.dashboard.DashboardListViewDataProvider;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.presenters.DashboardPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.view.list.ListViewDataProvider;

public class DashboardView extends ListView implements Display {
	final DashboardListViewDataProvider provider;

	public DashboardView(String module, final ReadServiceAsync readService) {
		super(module, readService);

		setPageSize(20);
		setShowTitle(true);

		this.provider = new DashboardListViewDataProvider(module, readService);
	}

	@Override
	protected ListViewDataProvider getListDataProvider() {
		return provider;
	}

	@Override
	public void insertRefreshedData(ListQueryResult value) {
		if (null != provider) {
			initialize();
			for (final HasData<Dto> display : provider.getDataDisplays()) {
				provider.insertRefreshedData(display, value);
			}
		}
	}
}
