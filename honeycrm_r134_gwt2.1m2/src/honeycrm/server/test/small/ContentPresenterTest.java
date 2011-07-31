package honeycrm.server.test.small;

import static org.easymock.EasyMock.createNiceMock;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.ContentPresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.test.small.mocks.ContentViewMock;
import junit.framework.TestCase;

import com.google.gwt.event.shared.SimpleEventBus;

public class ContentPresenterTest extends TestCase {
	private SimpleEventBus eventBus;
	private ReadServiceAsync readService;
	private UpdateServiceAsync updateService;
	private CreateServiceAsync createService;
	private ContentPresenter presenter;
	private Display view;

	@Override
	protected void setUp() throws Exception {
		this.view = new ContentViewMock();
		this.eventBus = new SimpleEventBus();
		this.readService = createNiceMock(ReadServiceAsync.class);
		this.updateService = createNiceMock(UpdateServiceAsync.class);
		this.createService = createNiceMock(CreateServiceAsync.class);
	}

	public void testCreateUninitialized() {
		try {
//			DtoModuleRegistry.create(null);
			presenter = new ContentPresenter(42L, view, eventBus, readService, updateService, createService);
//			fail();
		} catch (NullPointerException e) {
		}
	}

	public void testCreateInitialized() {
		DtoModuleRegistry.create(NewDtoWizard.getConfiguration());
		presenter = new ContentPresenter(42L, view, eventBus, readService, updateService, createService);
	}
}
