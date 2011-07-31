package honeycrm.server.test.small;

import static org.easymock.EasyMock.createNiceMock;
import honeycrm.client.LocalizedMessages;
import honeycrm.client.mvp.AppController;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.DeleteServiceAsync;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import junit.framework.TestCase;

import com.google.gwt.event.shared.SimpleEventBus;

public class AppControllerTest extends TestCase {
	private SimpleEventBus eventBus;
	private ReadServiceAsync readService;
	private AppController app;
	private LocalizedMessages constants;
	private CreateServiceAsync createService;
	private UpdateServiceAsync updateService;
	private ConfigServiceAsync confService;
	private PluginServiceAsync pluginService;
	private ReportServiceAsync reportService;
	private DeleteServiceAsync deleteService;
	private AuthServiceAsync authService;

	@Override
	protected void setUp() throws Exception {
		this.eventBus = new SimpleEventBus();
		this.readService = createNiceMock(ReadServiceAsync.class);
		this.app = new AppController(constants, readService, createService, updateService, deleteService, authService, confService, pluginService, reportService, eventBus);
	}
	
	public void testCreate() {
		this.app = new AppController(constants, readService, createService, updateService, deleteService, authService, confService, pluginService, reportService, eventBus);
	}
}
