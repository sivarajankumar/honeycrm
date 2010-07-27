package honeycrm.client;

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
		// super(clazz);
		
		listView = new ListView(clazz);
		listView.addStyleName("list_view");
		detailView = new DetailView(clazz);
		detailView.addStyleName("detail_view_content");

/*		final FlowPanel content = new FlowPanel();
		content.setStyleName("tab_content");

		final FlowPanel viewPanel = new FlowPanel();
		viewPanel.setStyleName("detail_view");
		viewPanel.add(new HTML("<div class='view_header_label'>Detail View</div>"));
		viewPanel.add(detailView);

		final VerticalPanel listPanel = new VerticalPanel();
		listPanel.setStyleName("list_view");
		listPanel.add(listView);
*/
		final SplitLayoutPanel splitPanel = new SplitLayoutPanel();
		splitPanel.addWest(new ScrollPanel(listView), 500);
		splitPanel.add(new ScrollPanel(detailView));
		//splitPanel.addStyleName("content");
		// splitPanel.forceLayout();
	//	splitPanel.setLayoutData()
		
/*		final FlowPanel hor = new FlowPanel();
		hor.setStyleName("content");
		hor.add(listPanel);
		hor.add(viewPanel);
		hor.add(new HTML("<div class='clear'></div>"));
*/
		addNorth(new ModuleButtonBar(clazz), 40);
	
		add(splitPanel);
//		content.add(new ModuleButtonBar(clazz));
		// add(splitPanel);
		
/*		FlowPanel h = new FlowPanel();
		h.add(listView);
		h.add(detailView);
		
		initWidget(h);*/
//		initWidget(content);
	}

	public void showDetailView(long id) {
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

	public void showCreateView() {
		detailView.getButtonBar().startCreating();
	}

	public boolean isListViewInitialized() {
		return listView.isInitialized();
	}

	public void showCreateViewPrefilled(String fieldId, Serializable value) {
		detailView.prefill(fieldId, value);
		showCreateView();
	}
}
