package honeycrm.client.mvp.presenters;

import com.google.gwt.user.client.ui.Widget;

public class HeaderPresenter {
	public interface Display {
		honeycrm.client.mvp.presenters.LoadPresenter.Display getLoadView();
		void attachPluginWidget(Widget w);
	}
}
