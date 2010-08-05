package honeycrm.client.view.csvimport;

import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.basiclayout.TabCenterView;
import honeycrm.client.csv.CsvImporter;
import honeycrm.client.misc.ServiceRegistry;

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
			public void onClick(final ClickEvent event) {
				popup.hide();
			}
		});

		final Button importBtn = new Button("Import");
		importBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				LoadIndicator.get().startLoading();

				final CsvImporter importer = CsvImporter.get(module);
				ServiceRegistry.commonService().importCSV(module, importer.parse(textArea.getText()), new AsyncCallback<Void>() {
					@Override
					public void onSuccess(final Void result) {
						LoadIndicator.get().endLoading();
						statusLabel.setText("Status: Import completed");
						TabCenterView.instance().get(module).refreshListView();
					}

					@Override
					public void onFailure(final Throwable caught) {
						LoadIndicator.get().endLoading();
						statusLabel.setText("Status: Import failed");
					}
				});
			}
		});

		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(cancelBtn);
		panel.add(importBtn);

		return panel;
	}

	public void show() {
		popup.show();
	}
}
