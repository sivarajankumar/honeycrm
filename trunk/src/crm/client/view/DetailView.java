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
import crm.client.dto.Viewable;

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

		buttonBar = new DetailViewButtonBar(this);

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

	public void refresh(final long id) {
		updateTitle(label.getTitle());
		LoadIndicator.get().startLoading();

		commonService.get(IANA.mashal(clazz), id, new AsyncCallback<Viewable>() {
			@Override
			public void onFailure(Throwable caught) {
				displayError(caught);
			}

			@Override
			public void onSuccess(Viewable result) {
				if (null == result) {
					Window.alert("Could not find account with id " + id);
				} else {
					// detailview should be responsible for rendering
					// only return the field types here
					refreshFields(result);
				}
				LoadIndicator.get().endLoading();
			}

		});
	}

	// TODO only update the field contents instead of removing all fields an adding them
	private void refreshFields(final Viewable viewable) {
		setFields(viewable, true);
	}

	private void setFields(final Viewable tmpViewable, final boolean readOnly) {
		final int[][] fieldIds = tmpViewable.getFormFieldIds();
		this.currentId = tmpViewable.getId();
		this.viewable = tmpViewable;

		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final int id = fieldIds[y][x];
				final Widget widgetLabel = getLabelForField(id);
				final Widget widgetValue = getWidgetByType(tmpViewable, id, readOnly);

				if (!readOnly) {
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

				table.setWidget(y, 2 * x + 0, widgetLabel);
				table.setWidget(y, 2 * x + 1, widgetValue);
			}
		}
	}

	/**
	 * Start editing mode.
	 */
	public void edit() {
		if (isShowing()) {
			setFields(viewable, false);
		}
	}

	/**
	 * Start view-only mode
	 */
	public void view() {
		if (isShowing()) {
			setFields(viewable, true);
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
					table.clear();
					currentId = -1;
				}
			});
		}
	}

	public void saveChanges() {
		save(table, currentId);
		refreshFields(viewable);
	}

	/**
	 * Returns true if this detailview is showing a proper entry. Otherwise false.
	 */
	public boolean isShowing() {
		return currentId != -1;
	}
}
