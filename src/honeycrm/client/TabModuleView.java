package honeycrm.client;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.view.AbstractView;
import honeycrm.client.view.DetailView;
import honeycrm.client.view.SearchWidget;
import honeycrm.client.view.SearchableListView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TabModuleView extends AbstractView {
	private final DetailView detailView;
	private final SearchableListView listView;
	private final SearchWidget searchWidget;

	public TabModuleView(final Class<? extends AbstractDto> clazz) {
		super(clazz);

		listView = new SearchableListView(clazz);
		searchWidget = new SearchWidget(clazz, listView);
		detailView = new DetailView(clazz);

		final VerticalPanel content = new VerticalPanel();
		content.setStyleName("tab_content");
		
		final Button createBtn = new Button("Create");
		createBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailView.getButtonBar().startCreating();
			}
		});
		createBtn.setStyleName("gwt-Button demo_button");
		final Button demoBtn = new Button("Demo");
		demoBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		});
		demoBtn.setStyleName("gwt-Button demo_button");
		
		final FlowPanel searchPanel = new FlowPanel();
		searchPanel.setStyleName("search_bar");
		searchPanel.add(createBtn);
		searchPanel.add(demoBtn);
		searchPanel.add(searchWidget);
		
		final FlowPanel viewPanel = new FlowPanel();
		viewPanel.setStyleName("detail_view");
		viewPanel.add(new HTML("<div class='view_header_label'>Detail View</div>"));
		viewPanel.add(detailView);
		
		detailView.setStyleName("detail_view_content");

		final VerticalPanel listPanel = new VerticalPanel();
		listPanel.setStyleName("list_view");
		listPanel.add(listView);
	
		final FlowPanel hor = new FlowPanel();
		hor.setStyleName("content");
		hor.add(listPanel);
		// hor.add(searchPanel);
		// hor.add(new HTML("<div class='horizontal_seperator'></div>"));
		// hor.add(listPanel);
		// hor.add(new HTML("<div class='horizontal_seperator'></div>"));
		hor.add(viewPanel);
		hor.add(new HTML("<div class='clear'></div>"));
		
		content.add(searchPanel);
		content.add(hor);

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
}
