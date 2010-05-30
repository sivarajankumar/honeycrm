package crm.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

public class FulltextSearchWidget extends SuggestBox {
	public static final int MIN_QUERY_LENGTH = 3;
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();

	public FulltextSearchWidget() {
		setStyleName("header_search_field");
		
		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				final String queryString = (getText() + event.getCharCode()).trim();

				if (queryString.length() < MIN_QUERY_LENGTH) {
					emptySuggestOracle();
				} else {
					startFulltextSearch(queryString);
				}
			}
		});
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
						final String label = a.getQuicksearchItem();

						if (quicksearchLabels.containsKey(label)) {
							quicksearchLabels.put(label, quicksearchLabels.get(label) + 1);
							o.add(label + " (" + quicksearchLabels.get(label) + ")"); // add a counter for this label since it is not unique
						} else {
							quicksearchLabels.put(label, 1);
							o.add(label);
						}
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
