package honeycrm.server.test.small;

import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.CreateEventHandler;
import honeycrm.client.mvp.presenters.DashboardPresenter;
import honeycrm.server.domain.Contact;
import honeycrm.server.test.small.mocks.DashboardViewMock;
import junit.framework.TestCase;

import com.google.gwt.event.shared.SimpleEventBus;

public class DashboardPresenterTest extends TestCase {
	private SimpleEventBus eventBus;
	private String module;
	private DashboardPresenter presenter;
	private DashboardViewMock view;
	private boolean createCalled;

	@Override
	protected void setUp() throws Exception {
		this.createCalled = false;
		this.eventBus = new SimpleEventBus();
		this.module = Contact.class.getSimpleName();
		this.view = new DashboardViewMock();
		this.presenter = new DashboardPresenter(eventBus, view, module);
	}

	public void testCreate() {
		assertNotNull(new DashboardPresenter(eventBus, view, module));
	}

	public void testClickAddButton() {
		eventBus.addHandler(CreateEvent.TYPE, new CreateEventHandler() {
			@Override
			public void onCreate(CreateEvent event) {
				if (event.getModule().equals(module)) {
					createCalled = true;
				}
			}
		});

		presenter.add();

		assertTrue(createCalled);
	}
}
