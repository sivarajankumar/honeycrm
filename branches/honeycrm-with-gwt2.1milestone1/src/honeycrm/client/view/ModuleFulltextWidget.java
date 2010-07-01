package honeycrm.client.view;

import honeycrm.client.FulltextSearchWidget;
import honeycrm.client.FulltextSuggestOracle;
import honeycrm.client.IANA;
import honeycrm.client.LoadIndicator;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.ListQueryResult;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ModuleFulltextWidget extends FulltextSearchWidget {
	private final Class<? extends AbstractDto> dtoClazz;
	
	public ModuleFulltextWidget(final Class<? extends AbstractDto> clazz) {
		super();
		this.dtoClazz = clazz;
	}
	
	@Override
	protected void startFulltextSearch(String queryString) {
		LoadIndicator.get().startLoading();

		commonService.fulltextSearchForModule(IANA.mashal(dtoClazz), queryString, 0, 10, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				LoadIndicator.get().endLoading();

				if (null != result && result.getItemCount() > 0) {
					final FulltextSuggestOracle o = emptySuggestOracle();
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
}
