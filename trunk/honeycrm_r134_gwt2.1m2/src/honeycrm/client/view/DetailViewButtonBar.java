package honeycrm.client.view;

import honeycrm.client.dto.ExtraButton;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.misc.WidgetJuggler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DetailViewButtonBar extends AbstractView implements ValueChangeHandler<String> {
	private final List<Button> editButtons = new ArrayList<Button>();
	private final List<Button> detailButtons = new ArrayList<Button>();

	public DetailViewButtonBar(final String module, final DetailView detailview) {
		super(module);

		addButtons(module, detailview);
		
		final HorizontalPanel panel = new HorizontalPanel();
		WidgetJuggler.addToContainer(panel, editButtons.toArray(new Widget[0]));
		WidgetJuggler.addToContainer(panel, detailButtons.toArray(new Widget[0]));
		initWidget(panel);

		History.addValueChangeHandler(this);
	}

	private void addButtons(final String module, final DetailView detailview) {
		editButtons.add(WidgetJuggler.getButton("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailview.saveChanges();
				// History.newItem(HistoryTokenFactory.get(module, ModuleAction.SAVE));
			}
		}));
		editButtons.add(WidgetJuggler.getButton("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(HistoryTokenFactory.get(module, ModuleAction.CANCEL));
			}
		}));
		
		for (final ExtraButton extraButton : moduleDto.getExtraButtons()) {
			final Button button = WidgetJuggler.getButton(extraButton.getLabel(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					extraButton.getAction().doAction(detailview.getCurrentDto());
				}
			});
			
			if (extraButton.getShow().equals(ModuleAction.DETAIL)) {
				detailButtons.add(button);
			} else { // assume the button should be visible in editing mode. add it to edit buttons.
				editButtons.add(button);
			}
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");

		if (2 <= token.length && token[0].equals(moduleDto.getModule())) {
			final ModuleAction action = ModuleAction.fromString(token[1]);

			if (null == action) {
				WidgetJuggler.setVisible(false, editButtons.toArray(new Widget[0]));
				WidgetJuggler.setVisible(false, detailButtons.toArray(new Widget[0]));
			} else {
				switch (action) {
				case SAVE:
				case CANCEL:
				case DETAIL:
					WidgetJuggler.setVisible(false, editButtons.toArray(new Widget[0]));
					WidgetJuggler.setVisible(true, detailButtons.toArray(new Widget[0]));
					break;
				default:
					startEditing();
					break;
				}
			}
		} else {
			WidgetJuggler.setVisible(false, editButtons.toArray(new Widget[0]));
			WidgetJuggler.setVisible(false, detailButtons.toArray(new Widget[0]));
		}
	}

	public void startEditing() {
		WidgetJuggler.setVisible(true, editButtons.toArray(new Widget[0]));
		WidgetJuggler.setVisible(false, detailButtons.toArray(new Widget[0]));
	}
}
