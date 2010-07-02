package honeycrm.client.view;

import honeycrm.client.dto.Dto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class SearchWidget extends AbstractView implements KeyPressHandler {
	private final FlowPanel panel = new FlowPanel();
	private final FlexTable table = new FlexTable();

	public SearchWidget(final Dto clazz, final SearchableListView listview) {
		super(clazz);

		final String[][] fieldIDs = new String[][] { new String[] { "name" } };

		final TextBox widgetValue = new TextBox();
		widgetValue.setWidth("400px");

		widgetValue.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					startSearch(listview, table, fieldIDs);
				}
			}
		});

		// table.setWidget(y, 2 * x + 0, widgetLabel);
		table.setWidget(0, 0, widgetValue);

		final Button searchBtn = new Button("Search");
		searchBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startSearch(listview, table, fieldIDs);
			}
		});

		final Button clearBtn = new Button("Clear");
		clearBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				emptyInputFields(table);
				listview.clearSearch();
			}
		});

		final Button markedBtn = new Button("Marked");
		markedBtn.addClickHandler(new ClickHandler() {
			private boolean showMarked = false;

			@Override
			public void onClick(ClickEvent event) {
				if (showMarked) {
					markedBtn.setText("Marked"); // allow user to see all items
					listview.refresh();
				} else {
					markedBtn.setText("All"); // allow user to see only marked items
					listview.getAllMarked();
				}
				showMarked = !showMarked;
			}
		});

		final Button advancedSearchBtn = new Button("Advanced Search");
		advancedSearchBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});

		// table.setStyleName("left");
		// table.setStyleName("search_field");

		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("left");
		buttonPanel.add(searchBtn);
		buttonPanel.add(clearBtn);
		buttonPanel.add(markedBtn);
		buttonPanel.add(advancedSearchBtn);

		panel.add(table);
		panel.add(buttonPanel);

		initWidget(panel);
	}

	private void startSearch(final SearchableListView listview, final FlexTable table, final String[][] fieldIDs) {
		initializeDtoFromTable(fieldIDs, table, dto); // build query by writing input from textboxes into
		// viewable instance
		listview.search(dto);
	}

	/**
	 * Open widget if closed. Close widget otherwise.
	 */
	@Override
	public void onKeyPress(KeyPressEvent event) {
		Window.alert("fired");
		/*
		 * if (event.isAltKeyDown() && 's' == event.getCharCode()) { panel.setOpen(!panel.isOpen()); }
		 */
	}
}
