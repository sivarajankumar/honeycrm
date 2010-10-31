package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.ListQueryResult;

import com.google.gwt.user.client.ui.HasWidgets;

public class DashboardPresenter implements Presenter {
	public interface Display {
		void insertRefreshedData(ListQueryResult value);
	}

	@Override
	public void go(HasWidgets container) {
	}
}
