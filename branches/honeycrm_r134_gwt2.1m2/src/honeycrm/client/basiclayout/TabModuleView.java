package honeycrm.client.basiclayout;

import honeycrm.client.misc.Callback;
import honeycrm.client.view.DetailView;
import honeycrm.client.view.ListView;
import honeycrm.client.view.ModuleButtonBar;

import java.io.Serializable;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class TabModuleView extends DockLayoutPanel {
	private final DetailView detailView;
	private final ListView listView;

	public TabModuleView(final String clazz) {
		super(Unit.PX);
		
		listView = new ListView(clazz);
		listView.addStyleName("list_view");
		detailView = new DetailView(clazz);
		detailView.addStyleName("detail_view_content");

		final SplitLayoutPanel splitPanel = new SplitLayoutPanel();
		splitPanel.addWest(new ScrollPanel(listView), 500);
		splitPanel.add(new ScrollPanel(detailView));

		addNorth(new ModuleButtonBar(clazz), 40);
	
		add(splitPanel);
	}

	public void openDetailView(long id) {
		// update url accordingly
		// History.newItem(viewable.getHistoryToken() + " " + id);
		detailView.refresh(id);
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

	public void openCreateView() {
		detailView.getButtonBar().startCreating();
	}

	public boolean isListViewInitialized() {
		return listView.isInitialized();
	}

	public void openCreateViewPrefilled(String fieldId, Serializable value) {
		detailView.prefill(fieldId, value);
		openCreateView();
	}
	
	public void openEditView(final long id) {
		detailView.refresh(id, new Callback() {
			@Override
			public void callback() {
				/**
				 *  start editing when the detail view has finished retrieving the specified item.
				 */
				detailView.getButtonBar().startEditing();
			}
		});
	}
}
