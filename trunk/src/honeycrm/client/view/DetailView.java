package honeycrm.client.view;

import org.apache.bcel.verifier.exc.LoadingException;

import honeycrm.client.LoadIndicator;
import honeycrm.client.RelationshipsContainer;
import honeycrm.client.dto.Dto;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget is responsible for displaying detail / edit / create - views for entities.
 */
public class DetailView extends AbstractView implements DoubleClickHandler {
	private final DetailViewButtonBar buttonBar;
	private final RelationshipsContainer relationshipsContainer;
	/**
	 * table containing the labels and the actual field values (or input fields if we are in edit mode).
	 */
	private FlexTable table = new FlexTable();
	private long currentId = -1; // id of currently displayed item

	public DetailView(final Dto clazz) {
		super(clazz);

		final VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.add(buttonBar = new DetailViewButtonBar(clazz, this));
		panel.add(new HTML("<div class='clear'></div>"));

		final HorizontalPanel hpanel = new HorizontalPanel();

		hpanel.add(panel);
		hpanel.add(relationshipsContainer = new RelationshipsContainer(clazz));

		buttonBar.setStyleName("detail_view_buttons");

		initWidget(hpanel);
	}

	/**
	 * Forces reload of all fields of the domain object. This is neccessary for updating fields that are set on server side and not visible while editing.
	 */
	public void refresh() {
		refresh(currentId);
	}

	public void refresh(final long id) {
		if (0 == id) {
			throw new RuntimeException("Cannot refresh because id == 0");
		} else {
			Prefetcher.instance.get(new Consumer<Dto>() {
				@Override
				public void setValueAsynch(Dto result) {
					table.setVisible(true);
					relationshipsContainer.refresh(id);

					if (null == result) {
						Window.alert("Could not find account with id " + id);
					} else {
						// detailview should be responsible for rendering only return the field
						// types here
						refreshFields(result);
						currentId = result.getId();
						buttonBar.startViewing();
					}
					LoadIndicator.get().endLoading();
				}
			}, new ServerCallback<Dto>() {
				@Override
				public void doRpc(final Consumer<Dto> internalCacheCallback) {
					LoadIndicator.get().startLoading();
					table.setVisible(false);
					
					commonService.get(dto.getModule(), id, new AsyncCallback<Dto>() {
						@Override
						public void onFailure(Throwable caught) {
							displayError(caught);
							table.setVisible(true);
							LoadIndicator.get().endLoading();
						}

						@Override
						public void onSuccess(Dto result) {
							internalCacheCallback.setValueAsynch(result);
						}
					});
				}
			}, 60 * 1000, dto.getModule(), id);
		}
	}

	// TODO only update the field contents instead of removing all fields an
	// adding them
	private void refreshFields(final Dto viewable) {
		resetFields(viewable, View.DETAIL);
	}

	private void resetFields(final Dto tmpViewable, final View view) {
		final String[][] fieldIds = tmpViewable.getFormFieldIds();
		this.currentId = tmpViewable.getId();
		this.dto = tmpViewable;

		// remove previous cell contents
		// TODO perhaps reuse widgets instead if this is faster.
		table.clear();

		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final String id = fieldIds[y][x];

				final Widget widgetLabel = getLabelForField(id);
				final Widget widgetValue = getWidgetByType(tmpViewable, id, view);

				widgetLabel.setStyleName("detail_view_label");

				if (view == View.DETAIL) {
					if (widgetValue instanceof Label) {
						((Label) widgetValue).addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								// the value of this field has been clicked. we assume the user
								// wanted to express that he would like to start editing the entity
								// so we start editing of this entity for him
								// TODO only do the following if this was a double click event. how
								// to check for this?
								// TODO additionally put the cursor into the text box of the field
								// that has been clicked
								buttonBar.startEditing();
							}
						});
					}
				} else {
					if (widgetValue instanceof TextBox) {
						((TextBox) widgetValue).addKeyDownHandler(new KeyDownHandler() {
							@Override
							public void onKeyDown(KeyDownEvent event) {
								if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
									saveChanges();
								}
							}
						});
					}
				}

				if (view == View.DETAIL || (view != View.DETAIL && !Dto.isInternalReadOnlyField(id))) {
					// display the widget because we are in readonly mode or we
					// are not in ro mode but it is no internal field
					table.setWidget(y, 2 * x + 0, widgetLabel);
					table.setWidget(y, 2 * x + 1, widgetValue);
				}
			}
		}
	}

	/**
	 * Start editing mode.
	 */
	public void startEditing() {
		if (isShowing()) {
			resetFields(dto, View.EDIT);
			relationshipsContainer.clear();
		} else {
			// Window.alert("Do nothing because id is not defined");
		}
	}

	/**
	 * Start view-only mode
	 */
	public void view() {
		if (isShowing()) {
			resetFields(dto, View.DETAIL);
		} else {
			// Window.alert("Do nothing because id is not defined");
		}
	}

	public void delete() {
		if (isShowing()) {
			LoadIndicator.get().startLoading();

			commonService.delete(dto.getModule(), currentId, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
					currentId = -1;
				}

				@Override
				public void onSuccess(Void result) {
					LoadIndicator.get().endLoading();
					stopViewing();
				}
			});
		}
	}

	public void saveChanges() {
		// when save has been completed the environment may request a refresh of the currently
		// displayed fields so we do not have to do this.
		save(table, currentId);
	}

	/**
	 * Returns true if this detailview is showing a proper entry. Otherwise false.
	 */
	public boolean isShowing() {
		return currentId != -1 && currentId != 0;
	}

	public void startCreating() {
		dto.setId(currentId = -1); // throw away previous id
		resetFields(dto, View.CREATE);
		relationshipsContainer.clear();
	}

	/**
	 * Throws away all input fields and resets currentId
	 */
	public void stopViewing() {
		table.clear();
		currentId = -1;
		buttonBar.stopViewing();
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		// TODO this event is never received
		if (isShowing()) {
			// the value of this field has been clicked. we assume the user
			// wanted to express that he would like to start editing the entity
			// so we start editing of this entity for him
			// TODO only do the following if this was a double click event. how
			// to check for this?
			// TODO additionally put the cursor into the text box of the field
			// that has been clicked
			buttonBar.startEditing();
		}
	}

	public DetailViewButtonBar getButtonBar() {
		return buttonBar;
	}
}
