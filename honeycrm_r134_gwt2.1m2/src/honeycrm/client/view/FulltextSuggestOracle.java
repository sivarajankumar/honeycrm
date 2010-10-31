package honeycrm.client.view;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;
import com.google.gwt.user.client.ui.SuggestOracle;

public class FulltextSuggestOracle extends SuggestOracle {
	private final List<MultiWordSuggestion> suggestions = new LinkedList<MultiWordSuggestion>();

	@Override
	public void requestSuggestions(Request request, Callback callback) {
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
