package honeycrm.client.basiclayout;

import honeycrm.client.admin.AdminWidget;
import honeycrm.client.admin.LogConsole;
import honeycrm.client.dashboard.Dashboard;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.reports.ReportSuggester;
import honeycrm.client.view.ModuleAction;
import honeycrm.client.view.csvimport.CsvImportWidget;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO update history token when a new tab is selected
public class TabCenterView extends TabLayoutPanel implements ValueChangeHandler<String> {
	private static TabCenterView instance = new TabCenterView();

	// use list instead of set to make sure the sequence stays the same
	private final Map<String, TabModuleView> moduleViewMap = new HashMap<String, TabModuleView>();
	/**
	 * Store the position for each module in the tab panel (e.g. Contacts -> Tab 0, Accounts -> Tab 1, ..)
	 */
	private final Map<String, Integer> tabPositionMap = new HashMap<String, Integer>();
	/**
	 * Store which module is set at which position in the tab panel (e.g. Tab 0 -> Contacts, Tab 1 -> Accounts, ..). This is almost the reverse version of tabPositionMap. However tabPositionMapReverse stores instances of Viewable instead storing classes.
	 */
	private final Map<Integer, String> tabPositionMapReverse = new HashMap<Integer, String>();
	private final Map<Integer, Widget> tabPosToCreateBtnMap = new HashMap<Integer, Widget>();

	public static TabCenterView instance() {
		return instance;
	}

	private TabCenterView() {
		super(25, Unit.PX);
		
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				int tabPos = 0;

				addStyleName("with_margin");
				addStyleName("tab_layout");

				final Collection<ModuleDto> dtos = DtoModuleRegistry.instance().getDtos();

				for (final ModuleDto moduleDto : dtos) {
					if (moduleDto.isHidden()) {
						continue; // do not add this module to the tabs since it should be hidden
					}

					final TabModuleView view = new TabModuleView(moduleDto.getModule());
					final Widget createBtn = getCreateButton(moduleDto.getModule());

					moduleViewMap.put(moduleDto.getModule(), view);
					tabPositionMap.put(moduleDto.getModule(), tabPos);
					tabPositionMapReverse.put(tabPos++, moduleDto.getModule());
					tabPosToCreateBtnMap.put(tabPos - 1, createBtn);

					// refresh list view only for the first tab (which is the only visible tab at the beginning)
					if (0 == tabPos)
						view.refreshListView();

					add((view), getTitlePanel(moduleDto.getTitle(), createBtn));
				}

				add(new Dashboard(), "Dashboard"); // TODO insert as first tab
				add(new AdminWidget(), "Misc");
//				add(new SampleReport(), "Reports");
				add(new ReportSuggester(), "Reports");

				addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
					@Override
					public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
						// hide all create buttons
						for (final Integer pos : tabPosToCreateBtnMap.keySet()) {
							tabPosToCreateBtnMap.get(pos).setVisible(false);
						}
						if (tabPosToCreateBtnMap.containsKey(event.getItem())) {
							// show create button for currently selected tab
							tabPosToCreateBtnMap.get(event.getItem()).setVisible(true);
						}

						if (tabPositionMapReverse.containsKey(event.getItem())) {
							// add the history token for the module stored in this tab
							History.newItem(DtoModuleRegistry.instance().get(tabPositionMapReverse.get(event.getItem())).getHistoryToken());
						} else {
							// TODO add history for special tabs like admin panel
						}
					}
				});
				

				
				/**
				 * show the tab of the first module.
				 */
				History.newItem(tabPositionMapReverse.get(0));
			}
			
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Could not execute asynchronously");
			}
		});

		History.addValueChangeHandler(this);
	}

	private Widget getCreateButton(final String module) {
		final Hyperlink createBtn = new Hyperlink("Create", module + " create");
		createBtn.addStyleName("create_button");
		createBtn.setVisible(false);
		return createBtn;
	}

	private Widget getTitlePanel(final String title, final Widget createBtn) {
		final Label moduleTitle = new Label(title + "s");
		final HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.add(createBtn);
		titlePanel.add(moduleTitle);
		return titlePanel;
	}

	public TabModuleView get(String moduleName) {
		return moduleViewMap.get(moduleName);
	}

	/**
	 * Shows the module tab for the module described by the given class.
	 */
	public void openDetailView(final String module, final long id) {
		if (moduleViewMap.containsKey(module)) {
			showModuleTab(module);
		} else {
			LogConsole.log("Cannot switch to module '" + module + "'/" + id + ".");
		}
	}

	public void openEditView(final String module, final long id) {
		if (moduleViewMap.containsKey(module)) {
			showModuleTab(module);
			moduleViewMap.get(module).openEditView(id);
		} else {
			LogConsole.log("Cannot switch to module '" + module + "'/" + id + ".");
		}
	}

	public void showModuleTab(String module) {
		if (!tabPositionMap.containsKey(module) || !moduleViewMap.containsKey(module)) {
			Window.alert("Cannot switch to module: '" + module + "'");
			return;
		}

		if (!moduleViewMap.get(module).isListViewInitialized()) {
			moduleViewMap.get(module).refreshListView();
		}
		selectTab(tabPositionMap.get(module));
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().trim().split("\\s+");

		if (2 <= token.length) {
			final ModuleAction action = ModuleAction.fromString(token[1]);

			final String module = token[0];

			switch (action) {
			case CREATE:
			case DETAIL:
				showModuleTab(module);
				break;
			case IMPORT:
				new CsvImportWidget(module).show();
				break;
			default:
				break;
			}
		} else if (1 == token.length) {
			if (!tabPositionMap.containsKey(token[0])) {
				/**
				 * Show the first module if the requested module cannot be found.
				 */
				showModuleTab(tabPositionMapReverse.get(0));
			} else {
				showModuleTab(token[0]);
			}
		}
	}
}
