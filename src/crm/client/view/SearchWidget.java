package crm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import crm.client.dto.AbstractDto;

public class SearchWidget extends AbstractView {
	private final FlexTable table = new FlexTable();
	
	public SearchWidget(final Class<? extends AbstractDto> clazz, final SearchableListView listview) {
		super(clazz);

		final int[][] fieldIDs = viewable.getSearchFields();
		for (int y = 0; y < fieldIDs.length; y++) {
			for (int x = 0; x < fieldIDs[y].length; x++) {
				final Label widgetLabel = new Label(viewable.getFieldById(fieldIDs[y][x]).getLabel());
				final Widget widgetValue = getWidgetByType(viewable, fieldIDs[y][x], false);

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
				
				table.setWidget(y, 2 * x + 0, widgetLabel);
				table.setWidget(y, 2 * x + 1, widgetValue);
			}
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

		table.setWidget(fieldIDs.length, 0, searchBtn);
		table.setWidget(fieldIDs.length, 1, clearBtn);
		
		initWidget(table);
	}
	
	private void startSearch(final SearchableListView listview, final FlexTable table, final int[][] fieldIDs) {
		setViewable(fieldIDs, table, viewable); // build query by writing input from textboxes into viewable instance
		listview.search(viewable);
	}
}
