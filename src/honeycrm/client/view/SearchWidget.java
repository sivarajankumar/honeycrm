package honeycrm.client.view;

import honeycrm.client.dto.AbstractDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class SearchWidget extends AbstractView implements KeyPressHandler {
	private final FlowPanel panel = new FlowPanel();
	private final FlexTable table = new FlexTable();
	
	public SearchWidget(final Class<? extends AbstractDto> clazz, final SearchableListView listview) {
		super(clazz);

		final int[][] fieldIDs = dto.getSearchFields();
		for (int y = 0; y < fieldIDs.length; y++) {
			for (int x = 0; x < fieldIDs[y].length; x++) {
				// final Label widgetLabel = new Label(dto.getFieldById(fieldIDs[y][x]).getLabel());
				final Widget widgetValue = getWidgetByType(dto, fieldIDs[y][x], View.EDIT);
				widgetValue.setWidth("400px");

				// attach key press event for starting search on hitting ENTER
				if (widgetValue instanceof TextBox) {
					((TextBox) widgetValue).addKeyPressHandler(new KeyPressHandler() {
						@Override
						public void onKeyPress(KeyPressEvent event) {
							if (event.getCharCode() == KeyCodes.KEY_ENTER) {
								startSearch(listview, table, fieldIDs);
							}
						}
					});
				}
				// TODO do this for other possible widgets as well

				// table.setWidget(y, 2 * x + 0, widgetLabel);
				table.setWidget(y, 2 * x + 1, widgetValue);
				
				break;
			}
			
			//TODO: display full text search for this module
			break;
		}

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

		table.setStyleName("search_field");
		
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("search_buttons");
		buttonPanel.add(searchBtn);
		buttonPanel.add(clearBtn);
		buttonPanel.add(markedBtn);

		panel.add(table);
		panel.add(buttonPanel);
		panel.add(new HTML("<div class='clear'></div>"));
		
		initWidget(panel);
	}

	private void startSearch(final SearchableListView listview, final FlexTable table, final int[][] fieldIDs) {
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
		if (event.isAltKeyDown() && 's' == event.getCharCode()) {
			panel.setOpen(!panel.isOpen());
		}
		*/
	}
}
