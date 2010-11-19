package honeycrm.server.test.small;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasText;

import honeycrm.client.mvp.events.RpcBeginEvent;
import honeycrm.client.mvp.events.RpcEndEvent;
import honeycrm.client.mvp.presenters.LoadPresenter;
import honeycrm.client.mvp.presenters.LoadPresenter.Display;
import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class LoadPresenterTest extends TestCase {
	private Display viewMock;
	private SimpleEventBus eventBus;
	private LoadPresenter presenter;

	@Override
	protected void setUp() throws Exception {
		this.eventBus = new SimpleEventBus();
		this.viewMock = createNiceMock(Display.class);
		
		this.viewMock.showLoadingIndicator(false);
		expectLastCall();
		this.viewMock.showLoadingIndicator(true);
		expectLastCall();
		
		final HasText h = new HasText() {
			private String text = "";
			@Override
			public void setText(final String text) {
				this.text = text;
			}
			
			@Override
			public String getText() {
				return text;
			}
		};
		
		replay(viewMock);
	}
	
	public void testCorrectInitialized() {
		this.presenter = new LoadPresenter(viewMock, eventBus);
		assertFalse(presenter.isLoading());
	}
	
	public void testOnOffBehaviour() {
		this.presenter = new LoadPresenter(viewMock, eventBus);

		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcEndEvent());
		assertFalse(presenter.isLoading());
	}
	
	public void testInterleavedOnOffBehaviour() {
		this.presenter = new LoadPresenter(viewMock, eventBus);

		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcEndEvent());
		assertTrue(presenter.isLoading());
		
		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcEndEvent());
		assertTrue(presenter.isLoading());

		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcBeginEvent());
		assertTrue(presenter.isLoading());
		
		eventBus.fireEvent(new RpcEndEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcEndEvent());
		assertTrue(presenter.isLoading());
		eventBus.fireEvent(new RpcEndEvent());
		assertTrue(presenter.isLoading());

		eventBus.fireEvent(new RpcEndEvent());
		assertFalse(presenter.isLoading());
	}
}
