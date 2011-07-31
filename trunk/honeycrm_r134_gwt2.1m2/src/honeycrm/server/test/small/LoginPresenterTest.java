package honeycrm.server.test.small;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import honeycrm.client.dto.Configuration;
import honeycrm.client.mvp.presenters.LoginPresenter;
import honeycrm.client.mvp.presenters.LoginPresenter.Display;
import honeycrm.client.services.AuthServiceAsync;
import honeycrm.client.services.ConfigServiceAsync;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.test.small.mocks.LoginViewMock;
import junit.framework.TestCase;

import org.easymock.IAnswer;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
		
		confService.getConfiguration(isA(AsyncCallback.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				final Object[] arguments = getCurrentArguments();
				AsyncCallback<Configuration> callback = (AsyncCallback<Configuration>) arguments[arguments.length - 1];
				callback.onSuccess(NewDtoWizard.getConfiguration());
				return null;
			}
		});	
		
		authService.login(isA(String.class), isA(String.class), isA(AsyncCallback.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				final Object[] arguments = getCurrentArguments();
				AsyncCallback<Long> callback = (AsyncCallback<Long>) arguments[arguments.length - 1];
				callback.onSuccess(42L);
				return null;
			}
		});
		
		replay(confService, authService);
	}
	
	public void testCreate() {
		presenter = new LoginPresenter(authService, confService, eventBus, loginView);
	}
	
	public void testTryLogin() {
		presenter = new LoginPresenter(authService, confService, eventBus, loginView);
		presenter.tryLogin();
	}
}
