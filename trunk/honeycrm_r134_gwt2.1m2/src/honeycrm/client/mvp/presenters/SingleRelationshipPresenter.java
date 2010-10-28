package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.ListQueryResult;

import com.google.gwt.user.client.ui.HasWidgets;

public class SingleRelationshipPresenter implements Presenter {
	public interface Display {
		void setId(Long relatedId);
		void insertRefreshedData(ListQueryResult relationshipData);
	}

	@Override
	public void go(HasWidgets container) {

	}
}
