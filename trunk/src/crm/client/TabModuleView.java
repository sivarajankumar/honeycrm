package crm.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import crm.client.dto.AbstractDto;
import crm.client.view.AbstractView;
import crm.client.view.DetailView;
import crm.client.view.SearchWidget;
import crm.client.view.SearchableListView;

public class TabModuleView extends AbstractView {
	private final DetailView detailView;
	private final SearchableListView listView;
	private final SearchWidget searchWidget;
	private final HTML relationView;

	public TabModuleView(final Class<? extends AbstractDto> clazz) {
		super(clazz);

		listView = new SearchableListView(clazz);
		searchWidget = new SearchWidget(clazz, listView);
		detailView = new DetailView(clazz);
		relationView = new HTML("<div class='view_header_label'>Relations</div>");

		final FlowPanel viewPanel = new FlowPanel();
		viewPanel.add(detailView);
		viewPanel.add(relationView);
		viewPanel.setStyleName("detail_view");
		detailView.setStyleName("detail_view_content");
		relationView.setStyleName("detail_view_relation");

		final VerticalPanel listPanel = new VerticalPanel();
		listPanel.add(listView);
		listPanel.setStyleName("list_view");

		final FlowPanel hor = new FlowPanel();

		VerticalPanel searchPanel = new VerticalPanel();

		searchPanel.setStyleName("actions");
//		searchPanel.add(new HTML("<div class='view_header_label'>Searchomat</div>"));
		// searchPanel.add(createView);
		searchPanel.add(searchWidget);
		searchPanel.add(listPanel);
		
		hor.setStyleName("tab_content");
		hor.add(searchPanel);
		// hor.add(searchPanel);
		// hor.add(new HTML("<div class='horizontal_seperator'></div>"));
		//hor.add(listPanel);
		// hor.add(new HTML("<div class='horizontal_seperator'></div>"));
		hor.add(viewPanel);
		hor.add(new HTML("<div class='clear'></div>"));

		initWidget(hor);
	}

	public void showDetailView(long id) {
		// update url accordingly
		// History.newItem(viewable.getHistoryToken() + " " + id);
		detailView.refresh(id);
	}

	public void saveCompleted() {
		listView.refresh();
		// TODO cannot call refresh because id of created entity is unknown
			detailView.refresh();
		//detailView.stopViewing();
	}
	
	public void saveCompletedForId(final long id) {
	listView.refresh();
		//saveCompleted();
		detailView.refresh(id);
	}

	public void refreshListView() {
		listView.refresh();
	}
}
