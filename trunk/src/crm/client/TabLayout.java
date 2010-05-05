package crm.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;

import crm.client.dto.AbstractDto;
import crm.client.view.LogConsole;

public class TabLayout extends Composite implements ValueChangeHandler<String> {
	private final Header header = new Header();
	private final TabCenterView center = TabCenterView.instance();

	public TabLayout() {
		DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);
		panel.setStyleName("body");
		center.setStyleName("tab_layout");

		panel.addNorth(header, 40);
		if (LogConsole.ENABLED)
			panel.addSouth(LogConsole.get(), 30);
		panel.add(center);

		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();

		LogConsole.log("created layout");

		initWidget(panel);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");

		if (2 == token.length) {
			center.showModuleTabWithId(AbstractDto.getViewableForHistoryToken(token[0]).getClass(), Long.valueOf(token[1]));
		} else if (1 == token.length) {
			center.showModuleTab(AbstractDto.getViewableForHistoryToken(token[0]).getClass());
		}
	}
}
