package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;

import honeycrm.client.dto.ModuleDto;
import honeycrm.client.mvp.events.CreateEvent;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.presenters.ModuleButtonBarPresenter;
import honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display;
import honeycrm.server.NewDtoWizard;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class ModuleButtonBarPresenterTest extends TestCase {
	private ModuleButtonBarPresenter presenter;
	private Display view;
	private SimpleEventBus eventBus;

	@Override
	protected void setUp() throws Exception {
		view = createNiceMock(Display.class);
		replay(view);

		eventBus = new SimpleEventBus();
	}

	public void testCreate() {
		for (final String module: NewDtoWizard.getConfiguration().getModuleDtos().keySet()) {
			presenter = new ModuleButtonBarPresenter(view, eventBus, module);
		}
	}
	
	public void testCreateEvent() {
		for (final ModuleDto moduleDto: NewDtoWizard.getConfiguration().getModuleDtos().values()) {
			presenter = new ModuleButtonBarPresenter(view, eventBus, moduleDto.getModule());
			eventBus.fireEvent(new CreateEvent(moduleDto.getModule()));
		}
	}
	
	public void testOpenEvent() {
		for (final ModuleDto moduleDto: NewDtoWizard.getConfiguration().getModuleDtos().values()) {
			presenter = new ModuleButtonBarPresenter(view, eventBus, moduleDto.getModule());
			eventBus.fireEvent(new OpenEvent(moduleDto.createDto()));
		}
	}
}
