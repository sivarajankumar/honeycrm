package honeycrm.client;

import honeycrm.client.admin.LogConsole;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class TabLayout extends DockLayoutPanel implements ValueChangeHandler<String> {
	private final Header header = new Header();
	private final TabCenterView center = TabCenterView.instance();

	public TabLayout() {
		super(Unit.PX);
		setStyleName("body");
		center.setStyleName("tab_layout");

		addNorth(header, 40);
		add(center);

		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();

		LogConsole.log("created layout");
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");
		
		if (2 == token.length) {
			if ("create".equals(token[1])) {
				center.showCreateViewForModule(token[0]);
			} else {
				center.showModuleTabWithId(token[0], Long.valueOf(token[1]));
			}
		} else if (1 == token.length) {
			center.showModuleTab(token[0]);
		}
	}
}
