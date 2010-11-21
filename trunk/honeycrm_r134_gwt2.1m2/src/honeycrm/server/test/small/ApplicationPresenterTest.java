package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.mvp.presenters.ApplicationPresenter;
import honeycrm.client.mvp.presenters.ApplicationPresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.PluginServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.server.services.ConfigServiceImpl;
import honeycrm.server.test.small.mocks.ContentViewMock;
import honeycrm.server.test.small.mocks.LoadViewMock;
import honeycrm.server.test.small.mocks.HeaderViewMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ApplicationPresenterTest extends TestCase {
	private ApplicationPresenter presenter;
	private ReadServiceAsync readService;
	private UpdateServiceAsync updateService;
	private CreateServiceAsync createService;
	private PluginServiceAsync pluginService;

	private SimpleEventBus eventBus;
	private Display applicationView;
	private honeycrm.client.mvp.presenters.LoadPresenter.Display loadView;
	private honeycrm.client.mvp.presenters.HeaderPresenter.Display headerView;
	private honeycrm.client.mvp.presenters.ContentPresenter.Display contentView;

	@Override
	protected void setUp() throws Exception {
		readService = createNiceMock(ReadServiceAsync.class);
		createService = createNiceMock(CreateServiceAsync.class);
		updateService = createNiceMock(UpdateServiceAsync.class);
		pluginService = createNiceMock(PluginServiceAsync.class);
		
		eventBus = new SimpleEventBus();
		loadView = new LoadViewMock();
		headerView = new HeaderViewMock(loadView);
		contentView = new ContentViewMock();
		applicationView = createNiceMock(ApplicationPresenter.Display.class);

		expect(applicationView.getHeader()).andReturn(headerView);
		expect(applicationView.getContentView()).andReturn(contentView);

	}

	public void testCreateUnitialized() {
		try {
			presenter = new ApplicationPresenter(42, readService, createService, updateService, pluginService, eventBus, applicationView);
			fail();
			// should not work because configuration has not been initialised yet.
		} catch (RuntimeException e) {
		}
	}
	
	public void testCreateInitialized() {
		replay(applicationView);
		
		DtoModuleRegistry.create(new ConfigServiceImpl().getConfiguration());
		presenter = new ApplicationPresenter(42, readService, createService, updateService, pluginService, eventBus, applicationView);
	}
}
