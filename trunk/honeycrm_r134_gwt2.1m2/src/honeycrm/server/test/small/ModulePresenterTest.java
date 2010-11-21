package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.mvp.presenters.ModulePresenter;
import honeycrm.client.mvp.presenters.ModulePresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.UpdateServiceAsync;
import honeycrm.server.test.small.mocks.ModuleViewMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ModulePresenterTest extends TestCase {
	private Display view;
	private ModulePresenter presenter;
	private ReadServiceAsync mockReadRpc;
	private UpdateServiceAsync mockUpdateRpc;
	private CreateServiceAsync mockCreateRpc;
	private SimpleEventBus eventBus;

	@Override
	protected void setUp() throws Exception {
		view = new ModuleViewMock();
		mockReadRpc = createStrictMock(ReadServiceAsync.class);
		mockUpdateRpc = createStrictMock(UpdateServiceAsync.class);
		mockCreateRpc = createStrictMock(CreateServiceAsync.class);
		eventBus = new SimpleEventBus();
	}

	public void testCreate() {
		presenter = new ModulePresenter("Contact", view, eventBus, mockReadRpc, mockUpdateRpc, mockCreateRpc);
	}
}
