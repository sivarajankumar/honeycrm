package honeycrm.client;

import honeycrm.client.view.AbstractView;
import honeycrm.client.view.DetailView;
import honeycrm.client.view.ListView;
import honeycrm.client.view.ModuleButtonBar;

import java.io.Serializable;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TabModuleView extends AbstractView {
	private final DetailView detailView;
	private final ListView listView;

	public TabModuleView(final String clazz) {
		super(clazz);

		listView = new ListView(clazz);
		detailView = new DetailView(clazz);
		detailView.setStyleName("detail_view_content");

		final FlowPanel content = new FlowPanel();
		content.setStyleName("tab_content");

		final FlowPanel viewPanel = new FlowPanel();
		viewPanel.setStyleName("detail_view");
		viewPanel.add(new HTML("<div class='view_header_label'>Detail View</div>"));
		viewPanel.add(detailView);

		final VerticalPanel listPanel = new VerticalPanel();
		listPanel.setStyleName("list_view");
		listPanel.add(listView);

		final FlowPanel hor = new FlowPanel();
		hor.setStyleName("content");
		hor.add(listPanel);
		hor.add(viewPanel);
		hor.add(new HTML("<div class='clear'></div>"));

		content.add(new ModuleButtonBar(clazz));
		content.add(hor);

/*		FlowPanel h = new FlowPanel();
		h.add(listView);
		h.add(detailView);
		
		initWidget(h);*/
		initWidget(content);
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
