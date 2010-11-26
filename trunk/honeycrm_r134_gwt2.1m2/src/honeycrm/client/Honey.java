package honeycrm.client;

import java.util.ArrayList;

import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.View;
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
import honeycrm.client.services.ReportService;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.client.services.UpdateService;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Honey implements EntryPoint {
	@Override
	public void onModuleLoad() {
		final ReadServiceAsync readService = GWT.create(ReadService.class);
		final ConfigServiceAsync configService = GWT.create(ConfigService.class);
		
		configService.getConfiguration(new AsyncCallback<Configuration>() {
			@Override
			public void onSuccess(Configuration result) {
				DtoModuleRegistry.create(result);
				
				final ServiceTableView v = new ServiceTableView();
				final ServiceTablePresenter p = new ServiceTablePresenter(v, View.EDIT, "UniqueService", readService);
				
				readService.getAll("UniqueService", 0, 1000, new AsyncCallback<ListQueryResult>() {
					@Override
					public void onSuccess(ListQueryResult result) {
						final ArrayList<Dto> list = new ArrayList<Dto>();
						for (final Dto d : result.getResults()) {
							list.add(d);
						}
						p.setValue(list);
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});

				RootLayoutPanel.get().add(v.asWidget());	
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		
		/*
		final CreateServiceAsync createSerice = GWT.create(CreateService.class);
		final UpdateServiceAsync updateService = GWT.create(UpdateService.class);
		final ReadServiceAsync readService = GWT.create(ReadService.class);
		final DeleteServiceAsync deleteService = GWT.create(DeleteService.class);
		final AuthServiceAsync authService = GWT.create(AuthService.class);
		final ConfigServiceAsync configService = GWT.create(ConfigService.class);
		final PluginServiceAsync pluginService = GWT.create(PluginService.class);
		final ReportServiceAsync reportService = GWT.create(ReportService.class);
		final LocalizedMessages constants = GWT.create(LocalizedMessages.class);
		
		final SimpleEventBus eventBus = new SimpleEventBus();
		final AppController appViewer = new AppController(constants, readService, createSerice, updateService, deleteService, authService, configService, pluginService, reportService, eventBus);
		appViewer.go(RootLayoutPanel.get());*/
	}
}
