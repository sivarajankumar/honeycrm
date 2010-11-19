package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.mvp.presenters.LoginPresenter;
import honeycrm.client.mvp.presenters.LoginPresenter.Display;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.server.test.small.mocks.LoginViewMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class LoginPresenterTest extends TestCase {
	private LoginPresenter presenter;
	private AuthServiceAsync authService;
	private ConfigServiceAsync confService;
	private Display loginView;
	private SimpleEventBus eventBus;
	
	@Override
	protected void setUp() throws Exception {
		eventBus = new SimpleEventBus();

		confService = createNiceMock(ConfigServiceAsync.class);
		authService = createNiceMock(AuthServiceAsync.class);
		loginView = new LoginViewMock();
		
		replay(confService, authService);
	}
	
	public void testCreate() {
		presenter = new LoginPresenter(authService, confService, eventBus, loginView);
	}
}
