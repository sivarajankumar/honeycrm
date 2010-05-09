package crm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.TabCenterView;
import crm.client.dto.AbstractDto;
import crm.client.dto.Viewable;

/**
 * Checkbox that is responsible for marking special dtos.
 */
public class MarkWidget extends AbstractView {
	public MarkWidget(final Class<? extends AbstractDto> clazz, final Viewable viewable) {
		super(clazz);

		final long id = viewable.getId();
		final CheckBox markBox = new CheckBox();
		
		markBox.setValue((Boolean) viewable.getFieldValue(AbstractDto.INDEX_MARKED));
		
		markBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoadIndicator.get().startLoading();

				commonService.mark(IANA.mashal(clazz), id, markBox.getValue(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						displayError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						TabCenterView.instance().get(clazz).markCompleted(); // tell container that mark has been completed
						LoadIndicator.get().endLoading();
					}
				});
			}
		});

		initWidget(markBox);
	}
}
