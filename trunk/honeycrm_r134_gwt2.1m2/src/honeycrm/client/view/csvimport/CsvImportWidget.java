package honeycrm.client.view.csvimport;

import honeycrm.client.basiclayout.TabCenterView;
import honeycrm.client.csv.CsvImporter;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.ServiceRegistry;
import honeycrm.client.mvp.views.LoadView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CsvImportWidget {
	private final DecoratedPopupPanel popup = new DecoratedPopupPanel();

	private final String module;

	public CsvImportWidget(final String module) {
		this.module = module;

		final TextArea textArea = getTextArea();
		final Label statusLabel = getStatusLabel();

		final VerticalPanel panel = new VerticalPanel();
		panel.add(getHeaderLabel());
		panel.add(textArea);
		panel.add(statusLabel);
		panel.add(getImportButton(textArea, statusLabel));

		popup.setWidget(panel);
		popup.center();
		popup.hide();
		popup.setGlassEnabled(true);
	}

	private Widget getHeaderLabel() {
		final Label label = new Label("CSV Import: Insert some SugarCRM CSV export data here.");
		return label;
	}

	private Label getStatusLabel() {
		final Label label = new Label("Status: ");
		return label;
	}

	private TextArea getTextArea() {
		final TextArea textArea = new TextArea();
		textArea.setSize("500px", "400px");
		return textArea;
	}

	private Widget getImportButton(final TextArea textArea, final Label statusLabel) {
		final Button cancelBtn = new Button("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});

		final Button importBtn = new Button("Import");
		importBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final CsvImporter importer = CsvImporter.get(module);
				importDto(importer.parse(textArea.getText()), 0, statusLabel);
			}
		});

		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(cancelBtn);
		panel.add(importBtn);

		return panel;
	}

	private void importDto(final Dto[] dtos, final int currentIndex, final Label statusLabel) {
		if (0 == currentIndex) {
			statusLabel.setText("Status: Started Import");
		}
		
		ServiceRegistry.createService().create(dtos[currentIndex], new AsyncCallback<Long>() {
			@Override
			public void onSuccess(Long result) {
				statusLabel.setText("Status: Imported " + String.valueOf(currentIndex) + " / " + dtos.length);

				final boolean isImportDone = currentIndex == dtos.length - 1;

				if (isImportDone) {
					statusLabel.setText("Status: Import completed");
					TabCenterView.instance().get(module).refreshListView();
				} else {
					importDto(dtos, 1 + currentIndex, statusLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				statusLabel.setText("Status: Import failed.");
			}
		});
	}

	public void show() {
		popup.show();
	}
}
