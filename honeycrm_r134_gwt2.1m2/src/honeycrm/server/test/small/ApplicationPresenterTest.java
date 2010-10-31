package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.mvp.presenters.ApplicationPresenter;
import honeycrm.client.mvp.presenters.ApplicationPresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.server.services.ConfigServiceImpl;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ApplicationPresenterTest extends TestCase {
	private ApplicationPresenter presenter;
	private ReadServiceAsync readService;
	private UpdateServiceAsync updateService;
	private CreateServiceAsync createService;

	private SimpleEventBus eventBus;
	private Display applicationView;

	@Override
	protected void setUp() throws Exception {
		readService = createMock(ReadServiceAsync.class);
		createService = createStrictMock(CreateServiceAsync.class);
		updateService = createStrictMock(UpdateServiceAsync.class);

		eventBus = new SimpleEventBus();
		applicationView = createStrictMock(ApplicationPresenter.Display.class);
	}

	public void testCreateUnitialized() {
		try {
			presenter = new ApplicationPresenter(1, readService, createService, updateService, eventBus, applicationView);
			fail();
			// should not work because configuration has not been initialised yet.
		} catch (RuntimeException e) {
		}
	}
	
	public void testCreateInitialized() {
		DtoModuleRegistry.create(new ConfigServiceImpl().getConfiguration());
		presenter = new ApplicationPresenter(1, readService, createService, updateService, eventBus, applicationView);
	}

	public void testFoo() {
		applicationView.getContentView();
	}
}
