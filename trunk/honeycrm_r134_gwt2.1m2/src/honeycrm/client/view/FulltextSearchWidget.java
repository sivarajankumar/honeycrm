package honeycrm.client.view;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.services.ReadServiceAsync;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
	private static final String INITIAL_SEARCH_TEXT = "Global Search ...";
	public static final int MIN_QUERY_LENGTH = 3;
	protected String lastQueryString;
	protected final Map<String, Dto> nameToDto = new HashMap<String, Dto>();
	private final ReadServiceAsync readService;

	public FulltextSearchWidget(final ReadServiceAsync readService) {
		super(new FulltextSuggestOracle());
		
		this.readService = readService;

		setupFocusAndBlur();

		addStyleName("wide_search_field");
		
		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
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
						// TODO defer full text search to avoid a lot requests after each other. start searching some time after user started typing.
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

	/**
	 * Display "Search..." if the user has not started typing yet.
	 */
	private void setupFocusAndBlur() {
		setText(INITIAL_SEARCH_TEXT);
		getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (getText().equals(INITIAL_SEARCH_TEXT)) {
					setText("");
				}
			}
		});
		getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (getText().isEmpty()) {
					setText(INITIAL_SEARCH_TEXT);
				}
			}
		});
	}

	protected void redirectToDetailView(SelectionEvent<Suggestion> event) {
		final String label = event.getSelectedItem().getReplacementString();

		if (nameToDto.containsKey(label)) {
			setText("");
			final Dto dto = nameToDto.get(label);
			History.newItem(HistoryTokenFactory.get(dto.getModule(), ModuleAction.DETAIL, dto.getId()));
		} else {
			Window.alert("Cannot determine id of selected item: '" + label + "'");
		}
	}

	protected void startFulltextSearch(final String queryString) {
		readService.fulltextSearch(queryString, 0, 10, new AsyncCallback<ListQueryResult>() {
		// commonService.fulltextSearch(queryString, 0, 10, new AsyncCallback<ListQueryResult>() {
			@Override
			public void onSuccess(ListQueryResult result) {
				if (null != result && result.getItemCount() > 0) {
					final FulltextSuggestOracle o = emptySuggestOracle();
					final Map<String, Integer> quicksearchLabels = new HashMap<String, Integer>();

					for (final Dto a : result.getResults()) {
						String label = a.getQuicksearch();

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
				Window.alert("fulltext search failed");
			}
		});
	}

	/**
	 * Empty the current suggestion oracle and return it.
	 */
	protected FulltextSuggestOracle emptySuggestOracle() {
		final FulltextSuggestOracle o = (FulltextSuggestOracle) getSuggestOracle();
		// final MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();
		o.clear();
		return o;
	}

}
