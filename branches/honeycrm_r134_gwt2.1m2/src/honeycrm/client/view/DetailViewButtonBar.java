package honeycrm.client.view;

import honeycrm.client.dto.ExtraButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public class DetailViewButtonBar extends AbstractView {
	public DetailViewButtonBar(final String module, final DetailView detailview) {
		super(module);

		final HorizontalPanel panel = new HorizontalPanel();

		addExtraButtons(panel, detailview);
		initWidget(panel);
	}

	private void addExtraButtons(final Panel panel, final DetailView detailview) {
		for (final ExtraButton extraButton : moduleDto.getExtraButtons()) {
			final Button b = new Button(extraButton.getLabel());
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					extraButton.getAction().doAction(detailview.getCurrentDto());
				}
			});
			
			panel.add(b);
		}
	}
}
