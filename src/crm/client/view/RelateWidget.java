package crm.client.view;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import crm.client.CommonServiceAsync;
import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.ServiceRegistry;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

public class RelateWidget extends SuggestBox {
	private long id;
	private final Class<? extends AbstractDto> clazz;
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();

	public RelateWidget(Class<? extends AbstractDto> clazz2, final long id) {
		super(new MultiWordSuggestOracle());
		this.clazz = clazz2;
		addHandlers();

		if (0 != id) {
			// an id of 0 indicates that nothing has been selected yet. if something has been selected yet load the actual name of the related account.
			this.id = id;
			setValueForId(id);
		}
	}

	private void setValueForId(final long id) {
		LoadIndicator.get().startLoading();
		
		commonService.get(IANA.mashal(DtoAccount.class), id, new AsyncCallback<Viewable>() {
			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
				setValue("Loading failed");
			}

			@Override
			public void onSuccess(Viewable result) {
				LoadIndicator.get().endLoading();

				if (null == result) {
					setValue("Not found");
				} else {
					setValue(((DtoAccount)result).getName());
				}
			}
		});
	}

	private void addHandlers() {
		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				final String query = getText().trim();

				// TODO cache some results instead of doing requests over and over again
				if (!query.isEmpty()) {
					LoadIndicator.get().startLoading();

					commonService.getAllByNamePrefix(IANA.mashal(DtoAccount.class), query, 0, 20, new AsyncCallback<ListQueryResult<? extends Viewable>>() {
						@Override
						public void onSuccess(ListQueryResult<? extends Viewable> result) {
							LoadIndicator.get().endLoading();

							if (0 == result.getResults().length) {
								indicateNoResults();
							} else {
								final MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();
								o.clear();

								for (final Viewable a : result.getResults()) {
									o.add(((DtoAccount)a).getName());
								}
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							LoadIndicator.get().endLoading();
							indicateNoResults();
						}

						private void indicateNoResults() {
							MultiWordSuggestOracle o = (MultiWordSuggestOracle) getSuggestOracle();

							o.clear();
							o.add("No Results");

							// TODO indicate that no results have been returned, set background color to red or something like that..
						}
					});
				}
			}
		});

		addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// determine id of this item and store the id in the gui to make sure it is available on submit
				if (DtoAccount.class == clazz) {
					final String selected = event.getSelectedItem().getReplacementString();

					commonService.getByName(IANA.mashal(clazz), selected, new AsyncCallback<Viewable>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Could not get id of selected item = " + selected);
						}

						@Override
						public void onSuccess(Viewable result) {
							if (null == result) {
								// the related entity could not be found or the search returned more than one result.
								// TODO what should be done in this case?
							} else {
								id = result.getId();
							}							
						}
					});
				}
			}
		});
	}

	/**
	 * Returns the id of the selected item.
	 */
	public long getId() {
		return id;
	}
}
