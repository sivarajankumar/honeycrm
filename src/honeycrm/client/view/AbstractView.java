package honeycrm.client.view;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.TabCenterView;
import honeycrm.client.dto.Dto;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

abstract public class AbstractView extends Composite {
	protected final CommonServiceAsync commonService = ServiceRegistry.commonService();
	protected Dto dto;

	public AbstractView(final Dto dto) {
		this.dto = dto;
	}

	protected String getTitleFromClazz() {
		return dto.getTitle();
	}

	/**
	 * Initialize a dto instance from the widgets (e.g., textboxes, dateboxes, relate fields) in a table.
	 */
	protected void initializeDtoFromTable(final String[][] fieldIds, final FlexTable table, final Dto tmpDto) {
		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final String id = fieldIds[y][x];

				if (!Dto.isInternalReadOnlyField(id)) {
					// TODO this position y, 2*x+1 depends on the current layout of the form..
					final Widget widgetValue = table.getWidget(y, 2 * x + 1);
					final Object value = tmpDto.getFieldById(id).getData(widgetValue);
					tmpDto.set(id, value);
				}
			}
		}
	}

	protected void save(final FlexTable table, final long id) {
		// id == -1 indicates that there is no id yet.
		// thus if id != -1 we know the id -> we do an update
		final boolean isUpdate = -1 != id && 0 != id;

		LoadIndicator.get().startLoading();

		if (isUpdate) {
			dto.setId(id);
		}
		initializeDtoFromTable(dto.getFormFieldIds(), table, dto);

		if (isUpdate) {
			commonService.update(dto, id, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					TabCenterView.instance().get(dto.getModule()).saveCompleted();
					LoadIndicator.get().endLoading();
				}

				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}
			});
		} else {
			commonService.create(dto, new AsyncCallback<Long>() {
				@Override
				public void onFailure(Throwable caught) {
					displayError(caught);
				}

				@Override
				public void onSuccess(Long result) {
					TabCenterView.instance().get(dto.getModule()).saveCompletedForId(result);
					// TabCenterView.instance().get(clazz).showDetailView(result);
					LoadIndicator.get().endLoading();
				}
			});
		}
	}

	protected Label getLabelForField(final String id) {
		return new Label(dto.getFieldById(id).getLabel() + ":");
	}

	/**
	 * Returns the widget for displaying fieldId of tmpViewable for the view.
	 */
	protected Widget getWidgetByType(final Dto tmpViewable, final String fieldId, final View view) {
		return tmpViewable.getFieldById(fieldId).getWidget(view, tmpViewable.get(fieldId));
	}

	/**
	 * Empties all input fields of the given {@link FlexTable}, e.g. after clearing a search widget.
	 */
	protected void emptyInputFields(final FlexTable table) {
		for (int y = 0; y < table.getRowCount(); y++) {
			for (int x = 0; x < table.getCellCount(y); x++) {
				final Widget w = table.getWidget(y, x);

				// clear the widget
				if (w instanceof TextBox) {
					((TextBox) w).setText("");
				} else if (w instanceof DateBox) {
					((DateBox) w).setValue(null);
				} else if (w instanceof RelateWidget) {
					((RelateWidget) w).setText("");
				}// else if (w instanceof )
			}
		}
	}

	protected void displayError(final Throwable caught) {
		LoadIndicator.get().endLoading();
		Window.alert(caught.getClass().toString());
		// throw new RuntimeException(caught.getLocalizedMessage());
	}

	public enum View {
		DETAIL, EDIT, CREATE, LIST, LIST_HEADER
	}
}
