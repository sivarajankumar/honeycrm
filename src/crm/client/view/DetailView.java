package crm.client.view;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.dto.AbstractDto;

/**
 * This widget is responsible for displaying detail / edit / create - views for entities.
 */
public class DetailView extends AbstractView {
	private static final String PREFIX = "Detail View";
	private final VerticalPanel panel = new VerticalPanel();
	private final Label label = new Label();
	private final DetailViewButtonBar buttonBar;
	/**
	 * table containing the labels and the actual field values (or input fields if we are in edit mode).
	 */
	private FlexTable table = new FlexTable();
	private long currentId = -1; // id of currently displayed item

	public DetailView(final Class<? extends AbstractDto> clazz) {
		super(clazz);

		// updateTitle(getTitleFromClazz());

		buttonBar = new DetailViewButtonBar(clazz, this);

		// css settings
		label.setStyleName("view_header_label");
		buttonBar.setStyleName("detail_view_buttons");

		label.setText(PREFIX);
		panel.add(label);
		panel.add(table);
		panel.add(buttonBar);
		panel.add(new HTML("<div class='clear'></div>"));

		initWidget(panel);
	}

	private void updateTitle(String title) {
		label.setText(PREFIX + title);
	}
	
	/**
	 * Forces reload of all fields of the domain object. This is neccessary for updating fields that are set on server side and not visible while editing.
	 */
	public void refresh() {
		refresh(currentId);
	}

	public void refresh(final long id) {
		updateTitle(label.getTitle());
		LoadIndicator.get().startLoading();

		commonService.get(IANA.mashal(clazz), id, new AsyncCallback<AbstractDto>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(AbstractDto result) {
				if (null == result) {
					Window.alert("Could not find account with id " + id);
				} else {
					// detailview should be responsible for rendering
					// only return the field types here
					refreshFields(result);
					buttonBar.startViewing();
				}
				LoadIndicator.get().endLoading();
			}

		});
	}

	// TODO only update the field contents instead of removing all fields an adding them
	private void refreshFields(final AbstractDto viewable) {
		setFields(viewable, View.DETAIL);
	}

	private void setFields(final AbstractDto tmpViewable, final View view) {
		final int[][] fieldIds = tmpViewable.getFormFieldIds();
		this.currentId = tmpViewable.getId();
		this.viewable = tmpViewable;

		// remove previous cell contents
		table.clear();
		
		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final int id = fieldIds[y][x];

				final Widget widgetLabel = getLabelForField(id);
				final Widget widgetValue = getWidgetByType(tmpViewable, id, view);

				if (view != View.DETAIL) {
					if (widgetValue instanceof TextBox) {
						((TextBox) widgetValue).addKeyDownHandler(new KeyDownHandler() {
							@Override
							public void onKeyDown(KeyDownEvent event) {
								if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
									saveChanges();
									buttonBar.startViewing();
									// button bar start viewing..
								}
							}
						});
					}
				}

				if (view == View.DETAIL || (view != View.DETAIL && !AbstractDto.isInternalReadOnlyField(id))) {
					// display the widget because we are in readonly mode or we are not in ro mode but it is no internal field
					table.setWidget(y, 2 * x + 0, widgetLabel);
					table.setWidget(y, 2 * x + 1, widgetValue);
				}
			}
		}
	}

	/**
	 * Start editing mode.
	 */
	public void edit() {
		if (isShowing()) {
			setFields(viewable, View.EDIT);
		}
	}

	/**
	 * Start view-only mode
	 */
	public void view() {
		if (isShowing()) {
			setFields(viewable, View.DETAIL);
		}
	}

	public void delete() {
		if (isShowing()) {
			LoadIndicator.get().startLoading();

			commonService.delete(IANA.mashal(clazz), currentId, new AsyncCallback<Void>() {
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
		save(table, currentId); // when save has been completed the environment may request a refresh of the currently displayed fields so we do not have to do this.
	}

	/**
	 * Returns true if this detailview is showing a proper entry. Otherwise false.
	 */
	public boolean isShowing() {
		return currentId != -1 && currentId != 0;
	}

	public void startCreating() {
		emptyInputFields(table);
		setFields(viewable, View.CREATE);
	}

	/**
	 * Throws away all input fields and resets currentId
	 */
	public void stopViewing() {
		table.clear();
		currentId = -1;
	}
}
