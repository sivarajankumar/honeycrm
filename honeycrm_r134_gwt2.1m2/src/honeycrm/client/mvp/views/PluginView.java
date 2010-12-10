package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.presenters.PluginPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class PluginView extends Composite implements Display {
	public static final LocalizedMessages constants = GWT.create(LocalizedMessages.class);
	private static PluginViewUiBinder uiBinder = GWT.create(PluginViewUiBinder.class);

	interface PluginViewUiBinder extends UiBinder<Widget, PluginView> {
	}

	@UiField FormPanel form;
	@UiField FileUpload upload;
	@UiField HTML area;
	@UiField Button submit;
	
	public PluginView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		submit.setText(constants.uploadPlugin());

		// Create a FormPanel and point it at a service.
		// final FormPanel form = new FormPanel();
		form.setAction("/Honey/upload");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		// VerticalPanel panel = new VerticalPanel();
		// form.setWidget(panel);

		// Create a FileUpload widget.
		// FileUpload upload = new FileUpload();
		upload.setName("file");
		// panel.add(upload);

		// final HTML area = new HTML();
		//panel.add(area);
		// final Button b = new Button("submit");
		//	public void onClick(ClickEvent event) {
		//		form.submit();
		//	}
		//});
		// panel.add(b);
		/*form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				plug
				pluginService.getPluginDescriptions(new AsyncCallback<PluginDescription[]>() {
					@Override
					public void onSuccess(PluginDescription[] result) {
						String html = "";

						for (final PluginDescription p : result) {
							html += "<li>" + p.getName() + " " + p.getDescription() + "</li>";
						}

						area.setHTML("<h1>Plugins</h1><ul>" + html + "</ul>");
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		});*/
		// RootPanel.get().add(form);
	}
	
	@UiHandler("submit")
	public void onSubmitHandler(ClickEvent event) {
		form.submit();
	}
}
