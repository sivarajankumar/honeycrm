package honeycrm.client.view;

import honeycrm.client.view.csvimport.CsvImportWidget;

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
	public ModuleButtonBar(final String module) {
		super(module);

		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("search_bar");
		panel.add(new ModuleFulltextWidget(module));
		panel.add(getAdvancedSearchButton());
		//panel.add(getExportButton("Export CSV", "csv"));
		//panel.add(getExportButton("Export XLS", "xls"));
		//panel.add(getExportButton("Export PDF", "pdf"));
		panel.add(getCsvImportButton(module));
 		panel.add(new HTML("<div class='clear'></div>"));

		initWidget(panel);
	}

	private Widget getAdvancedSearchButton() {
		// TODO implement on click event properly
		final Button button = new Button("Advanced Search");
		button.addStyleName("left");
		return button;
	}
	
	private Widget getCsvImportButton(final String module) {
		final Button button = new Button("CSV Import");
		button.addStyleName("right");
		
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CsvImportWidget(module).show();
			}
		});
		
		return button;		
	}
	
	private Button getExportButton(final String label, final String historyTokenAppendix) {
		final Button button = new Button(label);
		button.addStyleName("right");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(moduleDto.getHistoryToken() + " " + historyTokenAppendix);
			}
		});
		return button;
	}
}
