package honeycrm.client.view;

import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.ServiceRegistry;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class FulltextSearchWidget extends SuggestBox {
	public static final int MIN_QUERY_LENGTH = 3;
	protected String lastQueryString;
	protected final Map<String, Dto> nameToDto = new HashMap<String, Dto>();

	public FulltextSearchWidget() {
		super(new FulltextSuggestOracle());

		addStyleName("wide_search_field");

		addKeyPressHandler(getKeyPressHandler());
		addSelectionHandler(getSeletionHandler());
	}

	private SelectionHandler<Suggestion> getSeletionHandler() {
		return new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(final SelectionEvent<Suggestion> event) {
				redirectToDetailView(event);
			}
		};
	}

	private KeyPressHandler getKeyPressHandler() {
		return new KeyPressHandler() {
			@Override
			public void onKeyPress(final KeyPressEvent event) {
				if (KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
					setText("");
					emptySuggestOracle();
					return;
				}

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
		};
	}

	protected void redirectToDetailView(final SelectionEvent<Suggestion> event) {
		final String label = event.getSelectedItem().getReplacementString();

		if (nameToDto.containsKey(label)) {
			setText("");
			final Dto dto = nameToDto.get(label);
			History.newItem(dto.getHistoryToken() + " " + dto.getId());
		} else {
			Window.alert("Cannot determine id of selected item: '" + label + "'");
		}
	}

	protected void startFulltextSearch(final String queryString) {
		LoadIndicator.get().startLoading();

		ServiceRegistry.commonService().fulltextSearch(queryString, 0, 10, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(final ListQueryResult resultList) {
				LoadIndicator.get().endLoading();

				if (null != resultList && resultList.getItemCount() > 0) {
					final FulltextSuggestOracle oracle = emptySuggestOracle();
					final Map<String, Integer> quicksearchLabels = new HashMap<String, Integer>();

					for (final Dto result : resultList.getResults()) {
						String label = result.getQuicksearch();

						if (quicksearchLabels.containsKey(label)) {
							quicksearchLabels.put(label, quicksearchLabels.get(label) + 1);
							// add a counter for this label since it is not unique
							label = label + " (" + quicksearchLabels.get(label) + ")";
						} else {
							quicksearchLabels.put(label, 1);
						}

						// insert this label into the name to id translation map to make sure that
						// later the id can be determined by looking up the name in this map.
						nameToDto.put(label, result);

						oracle.add(label);
					}

					showSuggestionList();
				}
			}

			@Override
			public void onFailure(final Throwable caught) {
				LoadIndicator.get().endLoading();
				Window.alert("fulltext search failed");
			}
		});
	}

	/**
	 * Empty the current suggestion oracle and return it.
	 */
	protected FulltextSuggestOracle emptySuggestOracle() {
		final FulltextSuggestOracle oracle = (FulltextSuggestOracle) getSuggestOracle();
		// final MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();
		oracle.clear();
		return oracle;
	}

}
