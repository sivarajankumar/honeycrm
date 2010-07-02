package honeycrm.client.view;

import honeycrm.client.dto.Dto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class ModuleExportButtonBar extends AbstractView {
	public ModuleExportButtonBar(final Dto dtoClazz) {
		super(dtoClazz);

		final Button csvLink = getExportButton("CSV", "csv");
		final Button xlsLink = getExportButton("XLS", "xls");
		final Button pdfLink = getExportButton("PDF", "pdf");
		
		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("left");
		panel.add(csvLink);
		panel.add(xlsLink);
		panel.add(pdfLink);

		initWidget(panel);
	}

	private Button getExportButton(final String label, final String historyTokenAppendix) {
		final Button button = new Button(label);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(dto.getHistoryToken() + " " + historyTokenAppendix);
			}
		});
		return button;
	}
}
