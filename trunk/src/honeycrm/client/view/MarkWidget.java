package honeycrm.client.view;

import honeycrm.client.LoadIndicator;
import honeycrm.client.TabCenterView;
import honeycrm.client.dto.Dto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * Checkbox that is responsible for marking special dtos.
 */
public class MarkWidget extends AbstractView {
	public MarkWidget(final Dto clazz, final Dto viewable) {
		super(clazz);

		final long id = viewable.getId();
		final CheckBox markBox = new CheckBox();

		markBox.setValue(viewable.getMarked());

		markBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoadIndicator.get().startLoading();

				commonService.mark(clazz.getModule(), id, markBox.getValue(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						displayError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						// tell container that mark has been completed
						TabCenterView.instance().get(dto.getModule()).refreshListView();
						LoadIndicator.get().endLoading();
					}
				});
			}
		});

		initWidget(markBox);
	}
}
