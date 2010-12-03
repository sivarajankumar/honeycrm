package honeycrm.client.mvp.views;

import java.util.Collection;
import java.util.HashMap;

import honeycrm.client.LocalizedMessages;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.mvp.presenters.ContentPresenter;
import honeycrm.client.mvp.presenters.DashboardsPresenter;
import honeycrm.client.mvp.presenters.ModulePresenter;
import honeycrm.client.mvp.presenters.ContentPresenter.Display;
import honeycrm.client.services.ReadServiceAsync;
import honeycrm.client.services.ReportServiceAsync;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentView extends Composite implements Display {
	private final HashMap<String, ModulePresenter.Display> moduleViewMap = new HashMap<String, ModulePresenter.Display>();
	private final HashMap<String, Integer> tabPositionMap = new HashMap<String, Integer>();
	private final HashMap<Integer, String> tabPositionMapReverse = new HashMap<Integer, String>();
	private final HashMap<Integer, Widget> tabPosToCreateBtnMap = new HashMap<Integer, Widget>();
	private final DashboardsPresenter.Display dashboard;

	ContentPresenter presenter;

	final TabLayoutPanel panel;
	private final ReadServiceAsync readService;
	private final LocalizedMessages constants;

	public ContentView(final ReadServiceAsync readService, final ReportServiceAsync reportService, final LocalizedMessages constants) {
		initWidget(panel = new TabLayoutPanel(25, Unit.PX));
		
		this.readService = readService;
		this.constants = constants;

		panel.addStyleName("with_margin");
		panel.addStyleName("tab_layout");

		panel.add((Composite) (dashboard = new DashboardsView(readService, constants)), "Dashboard"); // TODO insert as first tab

		hiddenDirtyHack();
		inititializeLazy(1);
	}

	/**
	 * This overwrites the style attribute of the surrounding div element. Currently setting the style attribut only works deferred.
	 * This is just a big hack and should be done properly asap. 
	 */
	protected void hiddenDirtyHack() {
		new Timer() {
			@Override
			public void run() {
				panel.getElement().getParentElement().setAttribute("style", "left: 0px; right: 0px; bottom: 0px; overflow: hidden; top: 75px; position: absolute;");
			}
		}.schedule(1000);
	}

	protected void inititializeLazy(int tabPos) {
		final Collection<ModuleDto> dtos = DtoModuleRegistry.instance().getDtos();

		for (final ModuleDto moduleDto : dtos) {
			if (moduleDto.isHidden()) {
				continue; // do not add this module to the tabs since it should be hidden
			}

			final ModuleView view = new ModuleView(moduleDto.getModule(), readService, constants);
			moduleViewMap.put(moduleDto.getModule(), view);
			final Widget createBtn = getCreateButton(moduleDto.getModule());

			// moduleViewMap.put(moduleDto.getModule(), view);
			tabPositionMap.put(moduleDto.getModule(), tabPos);
			tabPositionMapReverse.put(tabPos++, moduleDto.getModule());
			tabPosToCreateBtnMap.put(tabPos - 1, createBtn);

			panel.add(view, getTitlePanel(moduleDto.getTitle(), createBtn));
		}

//		panel.add(new AdminWidget(null, null), "Misc");
//		panel.add(new ReportSuggester(reportService), "Reports");

		panel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
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
	}

	@Override
	public HasBeforeSelectionHandlers<Integer> getPanel() {
		return panel;
	}

	@Override
	public ModulePresenter.Display getModuleViewByName(final String moduleName) {
		return moduleViewMap.get(moduleName);
	}

	@Override
	public void setPresenter(ContentPresenter presenter) {
		this.presenter = presenter;
	}

	private Widget getTitlePanel(final String title, final Widget createBtn) {
		final Label moduleTitle = new Label(title + "s");
		final HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.add(createBtn);
		titlePanel.add(moduleTitle);
		return titlePanel;
	}

	private Widget getCreateButton(final String module) {
		final Label createBtn = new Label("Create");
		createBtn.addStyleName("create_button");
		createBtn.setVisible(false);
		createBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (null != presenter) {
					presenter.onCreate(module);
				}
			}
		});
		return createBtn;
	}

	@Override
	public String getModuleAtPosition(Integer position) {
		return tabPositionMapReverse.get(position);
	}

	@Override
	public void showModule(String module) {
		panel.selectTab(tabPositionMap.get(module));
	}

	@Override
	public honeycrm.client.mvp.presenters.DashboardsPresenter.Display getDashboard() {
		return dashboard;
	}
}
