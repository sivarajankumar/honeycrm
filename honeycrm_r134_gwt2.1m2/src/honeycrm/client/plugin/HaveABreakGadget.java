package honeycrm.client.plugin;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HaveABreakGadget extends AbstractPlugin {
	private static final long serialVersionUID = -8107901172182372270L;
	private long workTime = 0;
	private long breakTime = 0;
	private boolean hasBreak = false;
	private static final long MAX_BREAK_TIME = 5 * 60 * 1000;
	private static final long MAX_WORK_TIME = 20 * 60 * 1000;
	private static final int UPDATE_INTERVAL = 100 * 1000;

	public HaveABreakGadget() { // for serialisation
	}
	
	@Override
	public Widget getWidget() {
		final Label label = new Label();

		new Timer() {
			@Override
			public void run() {
				label.setText(getTimeString());
			}
		}.scheduleRepeating(UPDATE_INTERVAL);
		
		return label;
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

	@Override
	public ModifactionPlace getModificationPlace() {
		return ModifactionPlace.HEADER;
	}
}
