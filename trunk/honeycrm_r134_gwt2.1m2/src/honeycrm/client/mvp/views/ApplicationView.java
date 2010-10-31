package honeycrm.client.mvp.views;

import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.ApplicationPresenter.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationView extends Composite implements Display {
	private static ApplicationViewUiBinder uiBinder = GWT.create(ApplicationViewUiBinder.class);

	@UiTemplate("ApplicationView.ui.xml")
	interface ApplicationViewUiBinder extends UiBinder<Widget, ApplicationView> {
	}
	
	@UiField
	ContentView content;
	@UiField
	HeaderView header;
	
	public ApplicationView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public ContentPresenter.Display getContentView() {
		return content;
	}

	@Override
	public HeaderView getHeader() {
		return header;
	}
}
