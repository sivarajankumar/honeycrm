package honeycrm.client.mvp.presenters;

import honeycrm.client.mvp.events.RpcBeginEvent;
import honeycrm.client.mvp.events.RpcBeginEventHandler;
import honeycrm.client.mvp.events.RpcEndEvent;
import honeycrm.client.mvp.events.RpcEndEventHandler;

import com.google.gwt.event.shared.SimpleEventBus;

public class LoadPresenter {
	public interface Display {
		void showLoadingIndicator(boolean isVisible);
	}

	private final Display view;
	private final SimpleEventBus eventBus;
	private int concurrentRpcs = 0;
	
	public LoadPresenter(final Display view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		view.showLoadingIndicator(false);
		
		eventBus.addHandler(RpcBeginEvent.TYPE, new RpcBeginEventHandler() {
			@Override
			public void onRpcBegin(final RpcBeginEvent event) {
				++concurrentRpcs;
				toggleIndicatorVisibility();
			}
		});
		eventBus.addHandler(RpcEndEvent.TYPE, new RpcEndEventHandler() {
			@Override
			public void onRpcEnd(final RpcEndEvent event) {
				--concurrentRpcs;
				if (concurrentRpcs < 0) {
					//current workaround for race condition
					concurrentRpcs = 0;
				}
				toggleIndicatorVisibility();
			}
		});
	}

	protected void toggleIndicatorVisibility() {
		view.showLoadingIndicator(isLoading());
	}

	public boolean isLoading() {
		return concurrentRpcs > 0;
	}
}
