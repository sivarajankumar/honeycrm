package crm.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import crm.client.dto.AbstractDto;
import crm.client.view.AbstractView;
import crm.client.view.CreateView;
import crm.client.view.DetailView;
import crm.client.view.SearchWidget;
import crm.client.view.SearchableListView;

public class TabModuleView extends AbstractView {
	private final CreateView createView;
	private final DetailView detailView;
	private final SearchableListView listView;
	private final SearchWidget searchWidget;
	private final HTML relationView;
	
	public TabModuleView(final Class<? extends AbstractDto> clazz) {
		super(clazz);
		
		createView = new CreateView(clazz);
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
		
		final Button demoBtn = new Button("Demo");
		demoBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoadIndicator.get().startLoading();
				
				commonService.addDemo(IANA.mashal(clazz), new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						LoadIndicator.get().endLoading();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();						
						Window.alert("Could not create demo contact");
					}
				});
				
				listView.refresh();
			}
		});
		
		searchPanel.setStyleName("actions");
		searchPanel.add(new HTML("<div class='view_header_label'>Tightomat</div>"));
		searchPanel.add(demoBtn);
		searchPanel.add(createView);
		searchPanel.add(searchWidget);
		
		hor.setStyleName("tab_content");
		hor.add(searchPanel);
		//hor.add(new HTML("<div class='horizontal_seperator'></div>"));
		hor.add(listPanel);
		//hor.add(new HTML("<div class='horizontal_seperator'></div>"));
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
		createView.cancel();
	}
}
