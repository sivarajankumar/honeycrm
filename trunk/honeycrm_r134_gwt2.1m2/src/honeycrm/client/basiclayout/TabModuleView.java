package honeycrm.client.basiclayout;

import honeycrm.client.misc.Callback;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.view.DetailView;
import honeycrm.client.view.ModuleAction;
import honeycrm.client.view.ModuleButtonBar;
import honeycrm.client.view.list.ListView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class TabModuleView extends DockLayoutPanel {
	private DetailView detailView;
	private ListView listView;
	private final String module;

	public TabModuleView(final String module) {
		super(Unit.PX);
		this.module = module;

		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				listView = new ListView(module);
				listView.addStyleName("list_view");
				detailView = new DetailView(module);
				detailView.addStyleName("detail_view_content");

				final SplitLayoutPanel splitPanel = new SplitLayoutPanel();

				splitPanel.addStyleName("content_container");
				splitPanel.addWest(new ScrollPanel(listView), 500);
				splitPanel.add(new ScrollPanel(detailView));

				addNorth(new ModuleButtonBar(module, detailView), 50);

				add(splitPanel);
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Cannot run code asynchronously");
			}
		});
	}

	public void saveCompleted() {
		listView.refresh();
		detailView.refresh();
	}

	public void saveCompletedForId(final long id) {
		listView.refresh();
		detailView.refresh(id);
	}

	public void refreshListView() {
		listView.refresh();
	}

	public boolean isListViewInitialized() {
		return listView.isInitialized();
	}

	public void openEditView(final long id) {
		detailView.refresh(id, new Callback() {
			@Override
			public void callback() {
				/**
				 * start editing when the detail view has finished retrieving the specified item.
				 */
				History.newItem(HistoryTokenFactory.get(module, ModuleAction.EDIT));
			}
		});
	}
}
