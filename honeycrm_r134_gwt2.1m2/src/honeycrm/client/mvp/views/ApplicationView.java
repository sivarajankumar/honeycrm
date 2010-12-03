package honeycrm.client.mvp.views;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.ApplicationPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationView extends Composite implements Display {
	public static final LocalizedMessages constants = GWT.create(LocalizedMessages.class);
	private static ApplicationViewUiBinder uiBinder = GWT.create(ApplicationViewUiBinder.class);

	interface ApplicationViewUiBinder extends UiBinder<Widget, ApplicationView> {
	}
	
	@UiField
	ContentView content;
	@UiField
	HeaderView header;
	
	private final ReadServiceAsync readService;
	private final ReportServiceAsync reportService;
	
	public ApplicationView(final ReadServiceAsync readService, final ReportServiceAsync reportService) {
		this.readService = readService;
		this.reportService = reportService;
		
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
	
	@UiFactory
	ContentView makeContentView() {
		return new ContentView(readService, reportService, constants);
	}
	
	@UiFactory
	HeaderView makeHeader() {
		return new HeaderView(readService, constants);
	}
}
