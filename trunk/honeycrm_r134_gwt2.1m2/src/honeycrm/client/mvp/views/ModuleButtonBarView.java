package honeycrm.client.mvp.views;

import honeycrm.client.misc.WidgetJuggler;
import honeycrm.client.mvp.presenters.ModuleButtonBarPresenter.Display;
import honeycrm.client.view.ModuleAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget contains everything (buttons, fulltext search field) above the list view and detail view of the currently active module.
 */
public class ModuleButtonBarView extends Composite implements Display {
	private static ModuleButtonBarViewUiBinder uiBinder = GWT.create(ModuleButtonBarViewUiBinder.class);

	interface ModuleButtonBarViewUiBinder extends UiBinder<Widget, ModuleButtonBarView> {
	}

	@UiField Hyperlink cancel;
	@UiField Hyperlink save;
	@UiField Hyperlink edit;
	@UiField Hyperlink delete;
	@UiField Hyperlink changes;
	@UiField Hyperlink print;
	@UiField Hyperlink duplicate;
	@UiField Hyperlink findDuplicates;
	@UiField Hyperlink importBtn;
	@UiField Hyperlink export;
	@UiField Hyperlink search;
	
	public ModuleButtonBarView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancel.setText("Cancel");
		save.setText("Save");
		edit.setText("Edit");
		delete.setText("Delete");
		changes.setText("Changes");
		print.setText("Print");
		duplicate.setText("Duplicate");
		findDuplicates.setText("Find Duplicates");
		importBtn.setText("Import");
		export.setText("Export");
		search.setText("Advanced Search");
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return search;
	}

	@Override
	public void toggleVisibility(final ModuleAction action) {
		switch (action) {
		case EDIT:
		case CREATE:
			WidgetJuggler.setVisible(true, cancel, save);
			WidgetJuggler.setVisible(false, edit, delete, changes, print, duplicate, findDuplicates, importBtn, export);
			break;
		case INIT:
			WidgetJuggler.setVisible(false, cancel, save, edit, delete, changes, duplicate, print, findDuplicates);
			WidgetJuggler.setVisible(true, search, importBtn, export);
			break;
		case SAVE:
		case CANCEL:
		case DETAIL:
			WidgetJuggler.setVisible(true, edit, delete, changes, print, duplicate, findDuplicates, importBtn, export, search);
			WidgetJuggler.setVisible(false, save, cancel);
			break;
		case ADVANCEDSEARCH:
			
			break;
		default:
			// TODO
			break;
		}		
	}
	
/*	private Widget editBtn, deleteBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn, cancelBtn, saveBtn, searchBtn;
	private final ModuleDto moduleDto;

	public ModuleButtonBarView(final String module, final DetailView detailView) {
		this.moduleDto = DtoModuleRegistry.instance().get(module);

		final FlowPanel panel = new FlowPanel();
		panel.setStyleName("tool_bar");
		initWidget(panel);

		History.addValueChangeHandler(this);

		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				searchBtn = getButton("Search", ModuleAction.ADVANCEDSEARCH, "module_buttons", "left", "left_margin_small");
				editBtn = getButton("Edit", ModuleAction.EDIT, "module_buttons", "left", "left_margin_big");
				deleteBtn = getButton("Delete", ModuleAction.DELETE, "module_buttons", "left", "left_margin_small");
				cancelBtn = getButton("Cancel", ModuleAction.CANCEL, "module_buttons", "left", "left_margin_small");
				saveBtn = getButton("Save", ModuleAction.SAVE, "module_buttons", "left", "left_margin_big");
				changesBtn = getButton("Changes", ModuleAction.CHANGES, "module_buttons", "left", "left_margin_big");
				printBtn = getButton("Print", ModuleAction.PRINT, "module_buttons", "left", "left_margin_small");
				duplicateBtn = getButton("Duplicate", ModuleAction.DUPLICATE, "module_buttons", "left", "left_margin_big");
				findDuplicatesBtn = getButton("Find Duplicates", ModuleAction.FINDDUPLICATES, "module_buttons", "left", "left_margin_small");
				importBtn = getButton("Import", ModuleAction.IMPORT, "module_buttons", "left", "left_margin_big");
				exportBtn = getButton("Export", ModuleAction.EXPORT, "module_buttons", "left", "left_margin_small");

				// do not add module fulltext widget because we only want to see one search box
				// panel.add(new ModuleFulltextWidget(module));

				WidgetJuggler.addToContainer(panel, searchBtn, editBtn, deleteBtn, saveBtn, cancelBtn, changesBtn, printBtn, duplicateBtn, findDuplicatesBtn, importBtn, exportBtn);
				toggleButtonVisibility(ModuleAction.INIT);

				panel.add(new HTML("<div class='clear'></div>"));				
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not run code asynchronously");
			}
		});
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
		case ADVANCEDSEARCH:
			
			break;
		default:
			// TODO
			break;
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");

		if (2 <= token.length && token[0].equals(moduleDto.getModule())) {
			final ModuleAction action = ModuleAction.fromString(token[1]);

			if (null != action) {
				toggleButtonVisibility(action);
			}
		}
	}*/
}
