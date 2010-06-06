package crm.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

public class FulltextSearchWidget extends SuggestBox {
	public static final int MIN_QUERY_LENGTH = 3;
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private String lastQueryString;
	private final Map<String, AbstractDto> nameToDto = new HashMap<String, AbstractDto>();

	public FulltextSearchWidget() {
		setStyleName("header_search_field");

		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				final String queryString = (getText() + event.getCharCode()).trim();

				if (!queryString.equals(lastQueryString)) {
					if (queryString.length() < MIN_QUERY_LENGTH) {
						emptySuggestOracle();
					} else {
						startFulltextSearch(queryString);
					}

					lastQueryString = queryString;
				}
			}
		});

		addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				redirectToDetailView(event);
			}

		});
	}

	private void redirectToDetailView(SelectionEvent<Suggestion> event) {
		final String label = event.getSelectedItem().getReplacementString();

		if (nameToDto.containsKey(label)) {
			setText("");
			final AbstractDto dto = nameToDto.get(label);
			History.newItem(dto.getHistoryToken() + " " + dto.getId());
		} else {
			Window.alert("Cannot determine id of selected item: '" + label + "'");
		}
	}
	
	private void startFulltextSearch(final String queryString) {
		LoadIndicator.get().startLoading();

		commonService.fulltextSearch(queryString, 0, 10, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				LoadIndicator.get().endLoading();

				if (null != result && result.getItemCount() > 0) {
					final MultiWordSuggestOracle o = emptySuggestOracle();
					final Map<String, Integer> quicksearchLabels = new HashMap<String, Integer>();

					for (final AbstractDto a : result.getResults()) {
						String label = a.getQuicksearchItem();

						if (quicksearchLabels.containsKey(label)) {
							quicksearchLabels.put(label, quicksearchLabels.get(label) + 1);
							// add a counter for this label since it is not unique
							label = label + " (" + quicksearchLabels.get(label) + ")";
						} else {
							quicksearchLabels.put(label, 1);
						}

						// insert this label into the name to id translation map to make sure that
						// later the id can be determined by looking up the name in this map.
						nameToDto.put(label, a);

						o.add(label);
					}

					showSuggestionList();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
				Window.alert("fulltext search failed");
			}
		});
	}

	/**
	 * Empty the current suggestion oracle and return it.
	 */
	private MultiWordSuggestOracle emptySuggestOracle() {
		final MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();
		o.clear();
		return o;
	}

}
