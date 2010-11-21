package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;
import honeycrm.client.mvp.events.OpenReportEvent;
import honeycrm.client.mvp.presenters.ReportsSuggestionPresenter;
import honeycrm.client.mvp.presenters.ReportsSuggestionPresenter.Display;
import honeycrm.client.services.ReportServiceAsync;
import honeycrm.server.test.small.mocks.ReportsSuggestionViewMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ReportsSuggestionPresenterTest extends TestCase {
	private ReportServiceAsync reportService;
	private Display view;
	private ReportsSuggestionPresenter presenter;
	private SimpleEventBus eventBus;

	@Override
	protected void setUp() throws Exception {
		this.view = new ReportsSuggestionViewMock();
		this.eventBus = new SimpleEventBus();
		this.reportService = createMock(ReportServiceAsync.class);
	}

	public void testCreate() {
		this.presenter = new ReportsSuggestionPresenter(view, eventBus, reportService);
	}
	
	public void testOpenReport() {
/*		reportService.getReport(0, isA(AsyncCallback.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				final Object[] arguments = getCurrentArguments();
				AsyncCallback<ReportData<Map<Integer, Map<String, Integer>>>> callback = (AsyncCallback<ReportData<Map<Integer, Map<String, Integer>>>>) arguments[arguments.length - 1];
				callback.onSuccess(new ReportServiceImpl().getReport(0));
				return null;
			}
		});
		replay(reportService);
*/		
		this.presenter = new ReportsSuggestionPresenter(view, eventBus, reportService);
		eventBus.fireEvent(new OpenReportEvent(0));
	}
}
