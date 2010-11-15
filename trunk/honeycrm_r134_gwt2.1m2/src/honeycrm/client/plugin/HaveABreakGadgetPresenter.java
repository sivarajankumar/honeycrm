package honeycrm.client.plugin;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HaveABreakGadgetPresenter extends AbstractPlugin {
	public interface Display extends PluginView {
		HasText getLabel();
		Widget asWidget();
	}

	private static final long serialVersionUID = -8107901172182372270L;
	private long workTime = 0;
	private long breakTime = 0;
	private boolean hasBreak = false;
	private static final long MAX_BREAK_TIME = 5 * 60 * 1000;
	private static final long MAX_WORK_TIME = 20 * 60 * 1000;
	private static final int UPDATE_INTERVAL = 100 * 1000;
	private Display view;

	public HaveABreakGadgetPresenter() { // for serialisation
	}

	@Override
	public void internalRunPlugin() {
		view.getLabel().setText(":-)");

		platform.scheduleRepeating(new Command() {
			@Override
			public void execute() {
				view.getLabel().setText(getTimeString());
			}
		}, UPDATE_INTERVAL);

		platform.attachToHeader(view.asWidget());
	}

	private String getTimeString() {
		if (hasBreak) {
			if (breakTime >= MAX_BREAK_TIME) {
				hasBreak = false;
				breakTime = 0;
				return "WORK AGAIN!";
			} else {
				breakTime += UPDATE_INTERVAL;
				return "BREAK! still " + ((MAX_BREAK_TIME - breakTime) / 1000) + " seconds";
			}
		} else {
			if (workTime >= MAX_WORK_TIME) {
				hasBreak = true;
				workTime = 0;
				return "BREAK AGAIN!";
			} else {
				workTime += UPDATE_INTERVAL;
				return "WORK! still " + ((MAX_WORK_TIME - workTime) / 1000) + " seconds";
			}
		}
	}

	// @Override
	// protected void setView(PluginView view) {
	// this.view = (Display) view;
	// }

	// @Override
	// public PluginView getDefaultView() {
	// return new HaveABreakGadgetView();
	// return null;
	// }

	//@Override
	//protected PluginView getView() {
	//	return view;
	//}
}
