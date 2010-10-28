package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.mvp.presenters.DetailPresenter;
import honeycrm.client.mvp.presenters.ModulePresenter;
import honeycrm.client.mvp.presenters.ModulePresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ModulePresenterTest extends TestCase {
	private Display mockDisplay;
	private ModulePresenter presenter;
	private ReadServiceAsync mockReadRpc;
	private UpdateServiceAsync mockUpdateRpc;
	private CreateServiceAsync mockCreateRpc;
	private honeycrm.client.mvp.presenters.DetailPresenter.Display mockDetailView;

	@Override
	protected void setUp() throws Exception {
		mockDetailView = createStrictMock(DetailPresenter.Display.class);
		mockDisplay = createStrictMock(ModulePresenter.Display.class);
		mockReadRpc = createStrictMock(ReadServiceAsync.class);
		mockUpdateRpc = createStrictMock(UpdateServiceAsync.class);
		mockCreateRpc = createStrictMock(CreateServiceAsync.class);
		
		// replay(mockDisplay);
	}

	public void testFoo() {
		final DetailPresenter dp = new DetailPresenter(new SimpleEventBus(), "Contact", mockReadRpc, mockUpdateRpc, mockCreateRpc, mockDetailView);
		
		

		presenter = new ModulePresenter("Contact", mockDisplay, new SimpleEventBus(), mockReadRpc, mockUpdateRpc, mockCreateRpc);
	}
}
