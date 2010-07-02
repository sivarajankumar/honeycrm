package honeycrm.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;

public class FulltextSuggestOracle extends SuggestOracle {
	private final List<MultiWordSuggestion> suggestions = new LinkedList<MultiWordSuggestion>();

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		/*
		 * suggestions.add(new MultiWordSuggestion("a", "a")); suggestions.add(new MultiWordSuggestion("aa", "aa")); suggestions.add(new MultiWordSuggestion("aaa", "aaa")); suggestions.add(new MultiWordSuggestion("b", "b"));
		 */
		final Response response = new Response(suggestions);

		callback.onSuggestionsReady(request, response);
	}

	public void add(String label) {
		suggestions.add(new MultiWordSuggestion(label, label));
	}

	public void clear() {
		suggestions.clear();
	}
}
