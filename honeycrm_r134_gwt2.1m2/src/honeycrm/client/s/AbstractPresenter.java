package honeycrm.client.s;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractPresenter {
	public interface AbstractPresenterDisplay {
		Widget asWidget();
	}
	
	protected AbstractPresenterDisplay view;
	
	public final void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
