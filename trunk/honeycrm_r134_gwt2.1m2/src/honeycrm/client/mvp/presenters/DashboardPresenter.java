package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.mvp.views.HasSelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;

public class DashboardPresenter extends Selector {
	public interface Display extends HasSelectionHandler {
		void insertRefreshedData(ListQueryResult value);
	}

	public DashboardPresenter(final SimpleEventBus eventBus, final Display view) {
		super(eventBus, view);
	}
}
