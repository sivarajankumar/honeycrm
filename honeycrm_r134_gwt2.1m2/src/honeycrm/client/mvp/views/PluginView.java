package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.misc.PluginDescription;
import honeycrm.client.misc.PluginResponse;
import honeycrm.client.mvp.presenters.PluginPresenter;
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
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
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
	@UiField Button close;
	@UiField Label responseLabel;
	@UiField Label response;
	private PluginPresenter presenter;
	
	public PluginView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		close.setText(constants.cancel());
		submit.setText(constants.uploadPlugin());
		responseLabel.setText(constants.response());
		
		form.setAction("/Honey/upload");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		
		upload.setName("file");

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				presenter.onSubmitComplete();
			}
		});
	}
	
	@UiHandler("submit")
	public void onSubmitHandler(ClickEvent event) {
		form.submit();
	}

	@Override
	public void setPresenter(PluginPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setPlugins(PluginDescription[] result) {
		String html = "";

		for (final PluginDescription p : result) {
			html += "<li>" + p.getName() + " " + p.getDescription() + "</li>";
		}

		area.setHTML("<h1>Plugins</h1><ul>" + html + "</ul>");
	}

	@Override
	public void setResponse(PluginResponse result) {
		response.setText(result.getResponse());
	}
	
	@UiHandler("close")
	public void onClose(ClickEvent event) {
		removeFromParent();
	}
}
