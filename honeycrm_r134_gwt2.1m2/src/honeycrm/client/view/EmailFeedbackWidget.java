package honeycrm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmailFeedbackWidget extends Composite {
	private final TextArea box = getTextBox();
	private final Label status = new Label("Status: ");

	public EmailFeedbackWidget() {
		final VerticalPanel panel = new VerticalPanel();
		// panel.setStyleName("content");
		panel.add(box);
		panel.add(status);
		panel.add(getSubmitButton());
		initWidget(new ScrollPanel(panel));
	}

	private TextArea getTextBox() {
		final TextArea textArea = new TextArea();
		textArea.setText("Please enter your feedback:");
		textArea.setVisibleLines(10);
		textArea.setWidth("300px");
		return textArea;
	}

	private Widget getSubmitButton() {
		final Button button = new Button("Submit");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*ServiceRegistry.commonService().feedback(box.getText(), new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						status.setText("Status: Mail has been sent to mailing list. Thank you very much!");
					}

					@Override
					public void onFailure(Throwable caught) {
						status.setText("Status: An error occured during delivery");
					}
				});*/
			}
		});

		return button;
	}
}
