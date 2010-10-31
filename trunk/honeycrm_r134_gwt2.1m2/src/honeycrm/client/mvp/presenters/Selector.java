package honeycrm.client.mvp.presenters;

import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.events.OpenEvent;
import honeycrm.client.mvp.views.HasSelectionHandler;
import honeycrm.client.mvp.views.SelectionHandler;

import com.google.gwt.event.shared.SimpleEventBus;

abstract public class Selector {
	public Selector(final SimpleEventBus eventBus, final HasSelectionHandler hasSelectionHandler) {
		hasSelectionHandler.setSelectionHandler(new SelectionHandler() {
			@Override
			public void onSelect(Dto dto) {
				eventBus.fireEvent(new OpenEvent(dto));
			}
		});
	}
}
