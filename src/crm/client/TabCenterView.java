package crm.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DecoratedTabPanel;

import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.DtoEmployee;
import crm.client.dto.DtoMembership;
import crm.client.view.LogConsole;

// TODO update history token when a new tab is selected
public class TabCenterView extends DecoratedTabPanel {
	private static final TabCenterView instance = new TabCenterView();

	// use list instead of set to make sure the sequence stays the same
	private final List<AbstractDto> modules = new ArrayList<AbstractDto>();
	private final Map<Class<? extends AbstractDto>, TabModuleView> moduleViewMap = new HashMap<Class<? extends AbstractDto>, TabModuleView>();
	/**
	 * Store the position for each module in the tab panel (e.g. Contacts -> Tab 0, Accounts -> Tab 1, ..)
	 */
	private final Map<Class<? extends AbstractDto>, Integer> tabPositionMap = new HashMap<Class<? extends AbstractDto>, Integer>();
	/**
	 * Store which module is set at which position in the tab panel (e.g. Tab 0 -> Contacts, Tab 1 -> Accounts, ..). This is almost the reverse version of tabPositionMap. However tabPositionMapReverse stores instances of Viewable instead storing classes.
	 */
	private final Map<Integer, AbstractDto> tabPositionMapReverse = new HashMap<Integer, AbstractDto>();

	public static TabCenterView instance() {
		return instance;
	}

	private TabCenterView() {
		// add new modules here
		// ask server which modules exist
		modules.add(AbstractDto.getViewable(DtoContact.class));
		modules.add(AbstractDto.getViewable(DtoAccount.class));
		modules.add(AbstractDto.getViewable(DtoEmployee.class));
		modules.add(AbstractDto.getViewable(DtoMembership.class));

		int tabPos = 0;
		for (final AbstractDto viewable : modules) {
			final Class<? extends AbstractDto> clazz = viewable.getClass();
			final TabModuleView view = new TabModuleView(clazz);

			moduleViewMap.put(clazz, view);
			tabPositionMap.put(clazz, tabPos);
			tabPositionMapReverse.put(tabPos++, viewable);
			add(view, viewable.getTitle() + "s");
		}

		addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				// add the history token for the module stored in this tab
				History.newItem(tabPositionMapReverse.get(event.getItem()).getHistoryToken());
			}
		});

		LogConsole.log("created center view");

		selectTab(0);
	}

	public TabModuleView get(Class<? extends AbstractDto> clazz) {
		return moduleViewMap.get(clazz);
	}

	/**
	 * Shows the module tab for the module described by the given class.
	 */
	public void showModuleTabWithId(final Class<? extends AbstractDto> clazz, final long id) {
		showModuleTab(clazz);
		moduleViewMap.get(clazz).showDetailView(id);
	}

	public void showModuleTab(Class<? extends AbstractDto> clazz) {
		assert tabPositionMap.containsKey(clazz) && moduleViewMap.containsKey(clazz);
		selectTab(tabPositionMap.get(clazz));
	}
}
