package honeycrm.client.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget contains everything (buttons, fulltext search field) above the list view and detail view of the currently active module.
 */
public class ModuleButtonBar extends AbstractView {
	public ModuleButtonBar(final String dtoClazz) {
		super(dtoClazz);

		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("search_bar");
		panel.add(new ModuleFulltextWidget(dtoClazz));
		panel.add(getAdvancedSearchButton());
		panel.add(new ModuleExportButtonBar(dtoClazz));
		panel.add(new HTML("<div class='clear'></div>"));

		initWidget(panel);
	}

	private Widget getAdvancedSearchButton() {
		// TODO implement on click event properly
		final Button button = new Button("Advanced Search");
		button.setStyleName("gwt-Button left");
		return button;
	}
}
