package honeycrm.client.view;

import honeycrm.client.misc.WidgetJuggler;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget contains everything (buttons, fulltext search field) above the list view and detail view of the currently active module.
 */
public class ModuleButtonBar extends AbstractView implements ValueChangeHandler<String> {
	private final Widget editBtn, deleteBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn, cancelBtn, saveBtn, searchBtn;

	public ModuleButtonBar(final String module, final DetailView detailView) {
		super(module);

		searchBtn = getButton("Search", ModuleAction.ADVANCEDSEARCH, "left", "left_margin_small");
		editBtn = getButton("Edit", ModuleAction.EDIT, "left", "left_margin_big");
		deleteBtn = getButton("Delete", ModuleAction.DELETE, "left", "left_margin_small");
		cancelBtn = getButton("Cancel", ModuleAction.CANCEL, "left", "left_margin_small");
		saveBtn = getButton("Save", ModuleAction.SAVE, "left", "left_margin_big");
		changesBtn = getButton("Changes", ModuleAction.CHANGES, "left", "left_margin_big");
		printBtn = getButton("Print", ModuleAction.PRINT, "left", "left_margin_small");
		duplicateBtn = getButton("Duplicate", ModuleAction.DUPLICATE, "left", "left_margin_big");
		findDuplicatesBtn = getButton("Find Duplicates", ModuleAction.FINDDUPLICATES, "left", "left_margin_small");
		importBtn = getButton("Import", ModuleAction.IMPORT, "left", "left_margin_big");
		exportBtn = getButton("Export", ModuleAction.EXPORT, "left", "left_margin_small");

		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("search_bar");
		panel.add(new ModuleFulltextWidget(module));

		WidgetJuggler.addToContainer(panel, searchBtn, editBtn, deleteBtn, saveBtn, cancelBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn);
		toggleButtonVisibility(ModuleAction.INIT);

		panel.add(new HTML("<div class='clear'></div>"));

		History.addValueChangeHandler(this);

		initWidget(panel);
	}

	private Widget getButton(final String label, final ModuleAction state, final String... styles) {
		final String token = moduleDto.getHistoryToken() + " " + state.toString().toLowerCase();
		return WidgetJuggler.addStyles(new Hyperlink(label, token), styles);
	}

	private void toggleButtonVisibility(final ModuleAction state) {
		switch (state) {
		case EDIT:
		case CREATE:
			WidgetJuggler.setVisible(true, cancelBtn, saveBtn);
			WidgetJuggler.setVisible(false, editBtn, deleteBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn);
			break;
		case INIT:
			WidgetJuggler.setVisible(false, cancelBtn, saveBtn, editBtn, deleteBtn, changesBtn, duplicateBtn, printBtn, findDuplicatesBtn);
			WidgetJuggler.setVisible(true, searchBtn, importBtn, exportBtn);
			break;
		case SAVE:
		case CANCEL:
		case DETAIL:
			WidgetJuggler.setVisible(true, editBtn, deleteBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn, searchBtn);
			WidgetJuggler.setVisible(false, saveBtn, cancelBtn);
			break;
		default:
			// TODO
			break;
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");

		if (2 <= token.length && token[0].equals(moduleDto.getModule().toLowerCase())) {
			final ModuleAction action = ModuleAction.fromString(token[1]);

			if (null != action) {
				toggleButtonVisibility(action);
			}
		}
	}
}
