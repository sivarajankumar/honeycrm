package crm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;


public class ListViewPaginationBar extends Composite {

	private ListView listview;
	private Button delete = new Button("-");
	private Button first = new Button("<<");
	private Button left = new Button("<");
	private Button right = new Button(">");
	private Button last = new Button(">>");
	
	public ListViewPaginationBar(final ListView listview, Label label) {
		this.listview = listview;

		delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listview.deleteSelected();
			}
		});
		
		final HorizontalPanel underListPanel = new HorizontalPanel();
		underListPanel.setStyleName("under_list_panel");
		final HorizontalPanel paginationPanel = new HorizontalPanel();
		paginationPanel.setStyleName("list_pagination");
		
		underListPanel.add(delete);
		paginationPanel.add(first);
		paginationPanel.add(left);
		paginationPanel.add(label);
		paginationPanel.add(right);
		paginationPanel.add(last);
		underListPanel.add(paginationPanel);
		underListPanel.add(new HTML("<div class='clear'></div>"));
		
		setupHandlers();
		
		initWidget(underListPanel);
	}

	private void setupHandlers() {
		first.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listview.showFirstPage();
			}
		});
		
		left.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listview.showPageLeft();
			}
		});

		right.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listview.showPageRight();
			}
		});
		
		last.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listview.showLastPage();
			}
		});
	}
}
