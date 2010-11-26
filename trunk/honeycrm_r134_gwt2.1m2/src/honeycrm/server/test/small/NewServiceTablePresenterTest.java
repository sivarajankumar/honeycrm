package honeycrm.server.test.small;

import honeycrm.client.ServiceTablePresenter;
import honeycrm.client.ServiceTablePresenter.Display;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.misc.View;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.domain.Contract;
import honeycrm.server.test.small.mocks.NewServiceTableViewMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class NewServiceTablePresenterTest extends TestCase {
	private ServiceTablePresenter presenter;
	private Display view;
	private ReadServiceAsync readService;

	@Override
	protected void setUp() {
		DtoModuleRegistry.create(NewDtoWizard.getConfiguration());
		view = new NewServiceTableViewMock();
		readService = createNiceMock(ReadServiceAsync.class);
		
	/*	readService.getAll(isA(String.class), isA(Integer.class), isA(Integer.class), isA(AsyncCallback.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				final Object[] arguments = getCurrentArguments();
				AsyncCallback<ListQueryResult> callback = (AsyncCallback<ListQueryResult>) arguments[arguments.length - 1];
				callback.onSuccess(new ListQueryResult());
				return readService;
			}
		});
		
		replay(readService);*/
	}
	
	public void testCreate() {
		presenter = new ServiceTablePresenter(view, View.EDIT, Contract.class.getSimpleName(), "uniqueServices");
	}
}
