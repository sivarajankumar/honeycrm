package honeycrm.client;

import honeycrm.client.mvp.AppController;
import honeycrm.client.services.AuthService;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigService;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.CreateService;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteService;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.PluginService;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadService;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.client.services.UpdateService;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Honey implements EntryPoint {
	@Override
	public void onModuleLoad() {
		final CreateServiceAsync createSerice = GWT.create(CreateService.class);
		final UpdateServiceAsync updateService = GWT.create(UpdateService.class);
		final ReadServiceAsync readService = GWT.create(ReadService.class);
		final DeleteServiceAsync deleteService = GWT.create(DeleteService.class);
		final AuthServiceAsync authService = GWT.create(AuthService.class);
		final ConfigServiceAsync configService = GWT.create(ConfigService.class);
		final PluginServiceAsync pluginService = GWT.create(PluginService.class);
		final ReportServiceAsync reportService = GWT.create(ReportServiceAsync.class);
		
		final SimpleEventBus eventBus = new SimpleEventBus();
		final AppController appViewer = new AppController(readService, createSerice, updateService, deleteService, authService, configService, pluginService, reportService, eventBus);
		appViewer.go(RootLayoutPanel.get());
	}
}
