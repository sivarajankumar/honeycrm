package honeycrm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class ModuleExportButtonBar extends AbstractView {
	public ModuleExportButtonBar(final String dtoClazz) {
		super(dtoClazz);

		final Button csvLink = getExportButton("CSV", "csv");
		final Button xlsLink = getExportButton("XLS", "xls");
		final Button pdfLink = getExportButton("PDF", "pdf");

		final FlowPanel panel = new FlowPanel();
		panel.add(csvLink);
		panel.add(xlsLink);
		panel.add(pdfLink);

		initWidget(panel);
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
