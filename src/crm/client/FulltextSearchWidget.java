package crm.client;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

public class FulltextSearchWidget extends SuggestBox {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();

	public FulltextSearchWidget() {
		setStyleName("header_search_field");

		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				LoadIndicator.get().startLoading();
				
				commonService.fulltextSearch(getText(), 0, 10, new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
					@Override
					public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
						LoadIndicator.get().endLoading();
						
						final MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();
						o.clear();

						for (final AbstractDto a : result.getResults()) {
							o.add(a.getQuicksearchItem());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();
						Window.alert("fulltext search failed");
					}
				});
			}
		});
	}
}
