package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget contains everything (buttons, fulltext search field) above the list view and detail view of the currently active module.
 */
public class ModuleButtonBar extends AbstractView {
	public ModuleButtonBar(final Class<? extends AbstractDto> dtoClazz) {
		super(dtoClazz);

		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("search_bar");
		panel.add(getCreateButton());
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

	private Button getCreateButton() {
		final Button createBtn = new Button("Create");
		createBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(dto.getHistoryToken() + " create");
			}
		});

		createBtn.setStyleName("gwt-Button left");
		return createBtn;
	}
}
