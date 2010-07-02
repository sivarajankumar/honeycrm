package honeycrm.client.view.csvimport;

import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.csv.CsvImporter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContactCsvImportWidget extends Composite {
	public ContactCsvImportWidget() {
		final TextArea textArea = getTextArea();
		final Label statusLabel = getStatusLabel();

		final VerticalPanel panel = new VerticalPanel();
		panel.add(getHeaderLabel());
		panel.add(textArea);
		panel.add(statusLabel);
		panel.add(getImportButton(textArea, statusLabel));

		initWidget(panel);
	}

	private Widget getHeaderLabel() {
		final Label label = new Label("Contacts CSV Import");
		return label;
	}

	private Label getStatusLabel() {
		final Label label = new Label("Status: ");
		return label;
	}

	private TextArea getTextArea() {
		final TextArea textArea = new TextArea();
		textArea.setWidth("500px");
		return textArea;
	}

	private Button getImportButton(final TextArea textArea, final Label statusLabel) {
		final Button importBtn = new Button("Import");
		importBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoadIndicator.get().startLoading();

				final CsvImporter importer = new CsvImporter();
				ServiceRegistry.commonService().importContacts(importer.parse(textArea.getText()), new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						LoadIndicator.get().endLoading();
						statusLabel.setText("Status: Sucessfully imported contacts");
					}

					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();
						statusLabel.setText("Status: Could not import contacts");
					}
				});
			}
		});
		return importBtn;
	}
}
