package honeycrm.client.view;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.services.UpdateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;

/**
 * Checkbox that is responsible for marking special dtos.
 */
public class MarkWidget extends Composite {
	private final ModuleDto moduleDto;

	public MarkWidget(final Dto viewable, final UpdateServiceAsync updateService) {
		this.moduleDto = DtoModuleRegistry.instance().get(viewable.getModule());

		final long id = viewable.getId();
		final CheckBox markBox = new CheckBox();

		markBox.setValue(viewable.getMarked());

		markBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// mark dto on client side and do a usual update call on the server side.
				// this assumes the data in the dto is still up to date.
				viewable.set("marked", markBox.getValue());

				updateService.update(viewable, new AsyncCallback<Void>() {
//				commonService.mark(viewable.getModule(), id, markBox.getValue(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						// displayError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						// tell container that mark has been completed
						// TODO reimplement
						// TabCenterView.instance().get(moduleDto.getModule()).refreshListView();
					}
				});
			}
		});

		initWidget(markBox);
	}
}
