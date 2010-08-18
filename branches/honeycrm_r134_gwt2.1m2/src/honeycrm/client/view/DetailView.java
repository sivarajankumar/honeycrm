package honeycrm.client.view;

import honeycrm.client.admin.LogConsole;
import honeycrm.client.basiclayout.LoadIndicator;
import honeycrm.client.basiclayout.TabCenterView;
import honeycrm.client.dto.Dto;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget is responsible for displaying detail / edit / create - views for entities.
 */
public class DetailView extends AbstractView implements ValueChangeHandler<String> {
	private final DetailViewButtonBar buttonBar;
	private final RelationshipsContainer relationshipsContainer;
	private Dto dto = moduleDto.createDto();
	private Map<String, Serializable> prefilledMap = new HashMap<String, Serializable>();

	/**
	 * table containing the labels and the actual field values (or input fields if we are in edit mode).
	 */
	private FlexTable table = new FlexTable();

	public DetailView(final String module) {
		super(module);

		final VerticalPanel panel = new VerticalPanel();
		panel.add(table);
		panel.add(buttonBar = new DetailViewButtonBar(module, this));
		panel.add(relationshipsContainer = new RelationshipsContainer(module));

		History.addValueChangeHandler(this);
		
		initWidget(panel);
	}

	/**
	 * Forces reload of all fields of the domain object. This is necessary for updating fields that are set on server side and not visible while editing.
	 */
	public void refresh() {
		refresh(dto.getId());
	}
	
	public void refresh(final long id) {
		refresh(id, null);
	}
	
	/**
	 * Retrieve the item with the specified id, display its values in the detail view. Optionally execute the callback to tell clients when the item has successfully been retrieved.
	 */
	public void refresh(final long id, final Callback callback) {
		if (0 == id) {
			throw new RuntimeException("Cannot refresh because id == 0");
		} else {
			Prefetcher.instance.get(new Consumer<Dto>() {
				@Override
				public void setValueAsynch(Dto result) {
					table.setVisible(true);
					relationshipsContainer.setVisible(true);
					relationshipsContainer.refresh(id);

					if (null == result) {
						Window.alert("Could not find account with id " + id);
					} else {
						// detailview should be responsible for rendering only return the field
						// types here
						refreshFields(result);
						// currentId = result.getId();
					}
					LoadIndicator.get().endLoading();
					
					if (null != callback) {
						callback.callback();
					}
				}
			}, new ServerCallback<Dto>() {
				@Override
				public void doRpc(final Consumer<Dto> internalCacheCallback) {
					LoadIndicator.get().startLoading();
					table.setVisible(false);

					commonService.get(moduleDto.getModule(), id, new AsyncCallback<Dto>() {
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
			}, 60 * 1000, moduleDto.getModule(), id);
		}
	}

	// TODO only update the field contents instead of removing all fields an
	// adding them
	private void refreshFields(final Dto newDto) {
		resetFields(newDto, View.DETAIL, null);
	}

	private void resetFields(final Dto newDto, final View view) {
		resetFields(newDto, view, null);
	}

	private void resetFields(final Dto newDto, final View view, final String focussedField) {
		final String[][] fieldIds = newDto.getFormFieldIds();
		this.dto = newDto;

		// remove previous cell contents
		// TODO perhaps reuse widgets instead if this is faster.
		// table.clear();

		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final String id = fieldIds[y][x];

				final int labelX = 2 * x + 0, labelY = y;
				final int valueX = 2 * x + 1, valueY = y;

				final Widget widgetLabel = getWidgetLabel(id, labelX, labelY);
				final Widget widgetValue = getWidgetByType(dto, id, view);

				widgetLabel.setStyleName("detail_view_label");
				widgetValue.setStyleName("detail_view_value");

				addEvents(view, widgetLabel, widgetValue, id);
				addFocus(view, widgetValue, id, focussedField);

				if (view == View.DETAIL || (view != View.DETAIL && !Dto.isInternalReadOnlyField(id))) {
					// display the widget because we are in readonly mode or we
					// are not in ro mode but it is no internal field
					table.setWidget(labelY, labelX, widgetLabel);
					table.setWidget(valueY, valueX, widgetValue);
				}
			}
		}
	}

	private void addFocus(View view, Widget widgetValue, String id, String focussedField) {
		if (View.EDIT == view && null != focussedField && id.equals(focussedField) && widgetValue instanceof FocusWidget) {
			// TODO Cursor is still not put into the widget (e.g. text box) even with focus properly set.
			((FocusWidget) widgetValue).setFocus(true);
		}
	}

	/**
	 * Return the label widget and reuse the label that already has been attached whenever possible.
	 */
	private Widget getWidgetLabel(final String id, final int labelX, final int labelY) {
		final Widget widgetLabel;
		if (table.isCellPresent(labelY, labelX)) {
			if (table.getWidget(labelY, labelX) != null) {
				widgetLabel = table.getWidget(labelY, labelX);
			} else {
				widgetLabel = getLabelForField(id);
			}
		} else {
			widgetLabel = getLabelForField(id);
		}
		return widgetLabel;
	}

	private void addEvents(final View view, final Widget widgetLabel, final Widget widgetValue, final String focussedField) {
		if (view == View.DETAIL) {
			if (widgetLabel instanceof Label) {
				((Label) widgetLabel).addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// the label of this field has been clicked. we assume the user
						// wanted to express that he would like to start editing the entity
						// so we start editing of this entity for him
						History.newItem(HistoryTokenFactory.get(moduleDto.getModule(), ModuleAction.EDIT, focussedField));
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
	}

	/**
	 * Start editing mode.
	 */
	public void edit(final String focussedField) {
		if (isShowing()) {
			resetFields(dto, View.EDIT, focussedField);
			relationshipsContainer.setVisible(false);
		} else {
			LogConsole.log("startEditing(): Do nothing because id is not defined");
		}
	}

	/**
	 * Start view-only mode
	 */
	public void view() {
		if (isShowing()) {
			resetFields(dto, View.DETAIL);
			relationshipsContainer.setVisible(true);
		} else {
			// throw away the fields because we are not showing anything but should start viewing. this usually means the creation of a new entity has been cancelled.
			table.clear();
			LogConsole.log("view(): Do nothing because id is not defined");
		}
	}

	public void delete() {
		if (isShowing()) {
			LoadIndicator.get().startLoading();

			commonService.delete(moduleDto.getModule(), dto.getId(), new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}

				@Override
				public void onSuccess(Void result) {
					LoadIndicator.get().endLoading();
					TabCenterView.instance().get(moduleDto.getModule()).refreshListView();
					stopViewing();
				}
			});
		}
	}

	public void saveChanges() {
		// when save has been completed the environment may request a refresh of the currently
		// displayed fields so we do not have to do this.
		save(table, dto.getId());
	}

	/**
	 * Returns true if this detailview is showing a proper entry. Otherwise false.
	 */
	public boolean isShowing() {
		return dto.getId() > 0;
	}

	public void create() {
		resetFields(addPrefilledData(moduleDto.createDto()), View.CREATE);
		relationshipsContainer.setVisible(false);
	}

	/**
	 * Set all the fields in the given dto instance stored in the map storing prefilled fields.
	 */
	private Dto addPrefilledData(Dto dto) {
		for (int row = 0; row < dto.getFormFieldIds().length; row++) {
			for (int col = 0; col < dto.getFormFieldIds()[row].length; col++) {
				final String fieldId = dto.getFormFieldIds()[row][col];

				if (prefilledMap.containsKey(fieldId)) {
					dto.set(fieldId, prefilledMap.get(fieldId));
				}
			}
		}

		return dto;
	}

	/**
	 * Throws away all input fields and resets currentId
	 */
	public void stopViewing() {
		table.clear();
		dto.setId(-1);
		relationshipsContainer.setVisible(false);
	}

	public DetailViewButtonBar getButtonBar() {
		return buttonBar;
	}

	public void prefill(String fieldId, Serializable value) {
		prefilledMap.put(fieldId, value);
	}

	/**
	 * Returns the dto representation of the item that is currently displayed.
	 */
	public Dto getCurrentDto() {
		return dto;
	}

	private void cancel() {
		if (Window.confirm("Do you really want to cancel your changes?")) {
			view();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String[] token = event.getValue().split("\\s+");
		
		if (2 <= token.length && token[0].equals(moduleDto.getModule().toLowerCase())) {
			final ModuleAction action = ModuleAction.fromString(token[1]);
			
			if (null != action) {
				switch (action) {
				case CREATE:
					create();
					break;
				case EDIT:
					edit(null);
					break;
				case CANCEL:
					cancel();
					break;
				case DELETE:
					delete();
					break;
				case SAVE:
					saveChanges();
					break;
				default:
					break;
				}
			}
		}
	}
}
