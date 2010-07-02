package honeycrm.client;

import honeycrm.client.admin.AdminWidget;
import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;
import honeycrm.client.reports.SampleReport;
import honeycrm.client.view.EmailFeedbackWidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

// TODO update history token when a new tab is selected
public class TabCenterView extends DecoratedTabPanel {
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
	private final Map<Integer, Button> tabPosToCreateBtnMap = new HashMap<Integer, Button>();

	public static TabCenterView instance() {
		return instance;
	}

	private TabCenterView() {
		int tabPos = 0;

		// this.setSize("640px", "300px");

		final List<Dto> dtos = DtoRegistry.instance.getDtos();

		for (final Dto dto : dtos) {
			// final AbstractDto dto = DtoRegistry.instance.getDto(clazz);
			final TabModuleView view = new TabModuleView(dto);

			// refresh list view only for the first tab (which is the only visible tab at the beginning)
			if (0 == tabPos)
				view.refreshListView();

			// view.setSize("800px", "400px");

			moduleViewMap.put(dto.getModule(), view);
			tabPositionMap.put(dto.getModule(), tabPos);
			tabPositionMapReverse.put(tabPos++, dto.getModule());

			// TODO do not encapsulate view within ScrollPanel since it does not have the desired effect and messes up the layout.
			// TODO nevertheless, we need scrolling within the tabs.
			// final ScrollPanel scroll = new ScrollPanel(view);
			// scroll.setStyleName("tab_content");

			final Button createBtn = new Button("Create");
			createBtn.setVisible(false);
			createBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					view.showCreateView();
				}
			});/*
				 * createBtn.addMouseOverHandler(new MouseOverHandler() {
				 * 
				 * @Override public void onMouseOver(MouseOverEvent event) { setVisible(true); } });
				 */

			tabPosToCreateBtnMap.put(tabPos - 1, createBtn);

			final Label moduleTitle = new Label(dto.getTitle() + "s");
			/*
			 * moduleTitle.addClickHandler(new ClickHandler() {
			 * 
			 * @Override public void onClick(ClickEvent event) { createBtn.setVisible(true); } }); moduleTitle.addMouseOverHandler(new MouseOverHandler() {
			 * 
			 * @Override public void onMouseOver(MouseOverEvent event) { createBtn.setVisible(true); } }); moduleTitle.addMouseOutHandler(new MouseOutHandler() {
			 * 
			 * @Override public void onMouseOut(MouseOutEvent event) { createBtn.setVisible(false); } });
			 */

			final HorizontalPanel titlePanel = new HorizontalPanel();
			titlePanel.add(moduleTitle);
			titlePanel.add(createBtn);

			/*
			 * moduleTitle.addMouseOutHandler(new MouseOutHandler() {
			 * 
			 * @Override public void onMouseOut(MouseOutEvent event) { createBtn.setVisible(false); } });
			 */
			add((view), titlePanel);
		}

		add(new AdminWidget(), "Admin");
		add(new EmailFeedbackWidget(), "Feedback");
		add(new SampleReport(), "Reports");

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
					History.newItem(Dto.getByModuleName(dtos, tabPositionMapReverse.get(event.getItem())).getHistoryToken());
				} else {
					// TODO add history for special tabs like admin panel
				}
			}
		});

		LogConsole.log("created center view");

		selectTab(0);
	}

	public TabModuleView get(String moduleName) {
		return moduleViewMap.get(moduleName);
	}

	/**
	 * Shows the module tab for the module described by the given class.
	 */
	public void showModuleTabWithId(final String clazz, final long id) {
		showModuleTab(clazz);
		moduleViewMap.get(clazz).showDetailView(id);
	}

	public void showModuleTab(String clazz) {
		assert tabPositionMap.containsKey(clazz) && moduleViewMap.containsKey(clazz);
		if (!moduleViewMap.get(clazz).isListViewInitialized()) {
			moduleViewMap.get(clazz).refreshListView();
		}
		selectTab(tabPositionMap.get(clazz));
	}

	public void showCreateViewForModule(final String clazz) {
		moduleViewMap.get(clazz).showCreateView();
	}
}
