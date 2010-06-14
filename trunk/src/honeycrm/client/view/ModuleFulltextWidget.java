package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class ModuleFulltextWidget extends AbstractView {
	public ModuleFulltextWidget(final Class<? extends AbstractDto> clazz) {
		super(clazz);
		
		final TextBox box = new TextBox();
		box.setWidth("400px");

		final Panel panel = new FlowPanel();
		panel.setStyleName("left");
		panel.add(box);
		panel.add(new Button("Advanced Search"));

		initWidget(panel);
	}
}
