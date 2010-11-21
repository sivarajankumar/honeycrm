package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.mvp.presenters.RelationshipPresenter;
import honeycrm.client.mvp.presenters.RelationshipPresenter.Display;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.domain.Contact;
import honeycrm.server.test.small.mocks.HasClickHandlersMock;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class RelationshipPresenterTest extends TestCase {
	private SimpleEventBus eventBus;
	private Display view;
	private RelationshipPresenter presenter;

	@Override
	protected void setUp() throws Exception {
		this.eventBus = new SimpleEventBus();
		this.view = createNiceMock(Display.class);
		expect(view.getAddButton()).andReturn(new HasClickHandlersMock());
		expect(view.getOriginatingModule()).andReturn(Contact.class.getSimpleName());
		expect(view.getRelatedModule()).andReturn(Contact.class.getSimpleName());

		replay(view);
	}

	public void testCreate() {
		presenter = new RelationshipPresenter(eventBus, view);
	}

	public void testAddUnitialized() {
		try {
		//	DtoModuleRegistry.create(null);
			presenter = new RelationshipPresenter(eventBus, view);
			presenter.add();
		//	fail();
		} catch (NullPointerException e) {
		}
	}

	public void testAddInitialized() {
		presenter = new RelationshipPresenter(eventBus, view);

		DtoModuleRegistry.create(NewDtoWizard.getConfiguration());
		presenter.add();
	}
}
