package honeycrm.client;

import java.util.ArrayList;

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.SingleUploader;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import honeycrm.client.misc.PluginDescription;
import honeycrm.client.services.PluginService;
import honeycrm.client.services.PluginServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class Honey implements EntryPoint {
	private static final PluginServiceAsync pluginService = GWT.create(PluginService.class);

	// A panel where the thumbnails of uploaded images will be shown
	/*
	 * private FlowPanel panelImages = new FlowPanel(); private static final PluginServiceAsync pluginService = GWT.create(PluginService.class);
	 * 
	 * @Override public void onModuleLoad() { // Attach the image viewer to the document // RootPanel.get("thumbnails").add(panelImages = new FlowPanel());
	 * 
	 * // Create a new uploader panel and attach it to the document SingleUploader defaultUploader = new SingleUploader(FileInputType.LABEL); defaultUploader.setValidExtensions("jar", "JAR"); RootPanel.get().add(defaultUploader);
	 * 
	 * // Add a finish handler which will load the image once the upload finishes defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler); }
	 * 
	 * // Load the image in the document and in the case of success attach it to the viewer private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
	 * 
	 * @Override public void onFinish(IUploader uploader) { if (uploader.getStatus() == Status.SUCCESS) {
	 * 
	 * new PreloadedImage(uploader.fileUrl(), showImage);
	 * 
	 * // The server can send information to the client. // You can parse this information using XML or JSON libraries //Document doc = XMLParser.parse(uploader.getServerResponse()); //String size = Utils.getXmlNodeValue(doc, "file-1-size"); //String type = Utils.getXmlNodeValue(doc, "file-1-type"); // System.out.println(size + " " + type);
	 * 
	 * pluginService.getPluginNames(new AsyncCallback<ArrayList<String>>() {
	 * 
	 * @Override public void onSuccess(ArrayList<String> result) { System.out.println("Now the list of plugins is: "); for (final String r: result) { System.out.println(r); } }
	 * 
	 * @Override public void onFailure(Throwable caught) { } }); } } };
	 * 
	 * // Attach an image to the pictures viewer private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
	 * 
	 * @Override public void onLoad(PreloadedImage image) { image.setWidth("75px"); panelImages.add(image); } };
	 */

	public void onModuleLoad() {
		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		form.setAction("/Honey/upload");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("file");
		panel.add(upload);

		final HTML area = new HTML();
		panel.add(area);
		final Button b = new Button("submit");
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});
		panel.add(b);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
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
		});
		RootPanel.get().add(form);
	}
	/*
	 * @Override public void onModuleLoad() {
	 * 
	 * 
	 * 
	 * final CreateServiceAsync createSerice = GWT.create(CreateService.class); final UpdateServiceAsync updateService = GWT.create(UpdateService.class); final ReadServiceAsync readService = GWT.create(ReadService.class); final DeleteServiceAsync deleteService = GWT.create(DeleteService.class); final AuthServiceAsync authService = GWT.create(AuthService.class); final ConfigServiceAsync configService = GWT.create(ConfigService.class); final PluginServiceAsync pluginService = GWT.create(PluginService.class); final ReportServiceAsync reportService = GWT.create(ReportService.class); final LocalizedMessages constants = GWT.create(LocalizedMessages.class);
	 * 
	 * final SimpleEventBus eventBus = new SimpleEventBus(); final AppController appViewer = new AppController(constants, readService, createSerice, updateService, deleteService, authService, configService, pluginService, reportService, eventBus); appViewer.go(RootLayoutPanel.get()); }
	 */
}
