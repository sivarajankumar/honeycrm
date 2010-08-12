package honeycrm.client.view;

import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.view.csvimport.CsvImportWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget contains everything (buttons, fulltext search field) above the list view and detail view of the currently active module.
 */
public class ModuleButtonBar extends AbstractView implements ValueChangeHandler<String> {
	private final Widget editBtn;
	private Widget deleteBtn;
	private Widget changesBtn;
	private Widget printBtn;
	private Widget duplicateBtn;
	private Widget findDuplicatesBtn;
	private Widget importBtn;
	private Widget exportBtn;
	private Widget cancelBtn;
	private Widget saveBtn;
	private Widget searchBtn;

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
		// panel.add(getAdvancedSearchButton());
		// TODO setup css properly
		// panel.add(new DetailViewButtonBar(module, detailView));

		WidgetJuggler.addToContainer(panel, searchBtn, editBtn, deleteBtn, saveBtn, cancelBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn);
		toggleButtonVisibility(ModuleAction.INIT);

		// panel.add(getExportButton("Export CSV", "csv"));
		// panel.add(getExportButton("Export XLS", "xls"));
		// panel.add(getExportButton("Export PDF", "pdf"));
		// panel.add(getCsvImportButton(module));
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

	private Widget getAdvancedSearchButton() {
		// TODO implement on click event properly
		final Button button = new Button("Advanced Search");
		button.addStyleName("left");
		return button;
	}

	private Widget getCsvImportButton(final String module) {
		final Button button = new Button("CSV Import");
		button.addStyleName("right");

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CsvImportWidget(module).show();
			}
		});

		return button;
	}

	private Button getExportButton(final String label, final String historyTokenAppendix) {
		final Button button = new Button(label);
		button.addStyleName("right");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem(moduleDto.getHistoryToken() + " " + historyTokenAppendix);
			}
		});
		return button;
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
