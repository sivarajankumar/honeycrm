package crm.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import crm.client.CommonServiceAsync;
import crm.client.IANA;
import crm.client.LoadIndicator;
import crm.client.ServiceRegistry;
import crm.client.TabCenterView;
import crm.client.dto.AbstractDto;

abstract public class AbstractView extends Composite {
	// private static final NumberFormat DATE_FORMAT = DateFo NumberFormat.getFormat(LocaleInfo.getCurrentLocale().getNumberConstants().currencyPattern());
	protected final CommonServiceAsync commonService = ServiceRegistry.commonService();
	protected final Class<? extends AbstractDto> clazz;
	protected AbstractDto viewable;

	public AbstractView(final Class<? extends AbstractDto> clazz) {
		this.clazz = clazz;
		this.viewable = AbstractDto.getViewable(clazz);
	}

	protected String getTitleFromClazz() {
		return AbstractDto.getViewable(clazz).getTitle();
	}

	/**
	 * Initialize a viewable from the widgets (e.g., textboxes, dateboxes, relate fields) in a table.
	 */
	protected void setViewable(final int[][] fieldIds, final FlexTable table, final AbstractDto tmpViewable) {
		for (int y = 0; y < fieldIds.length; y++) {
			for (int x = 0; x < fieldIds[y].length; x++) {
				final int id = fieldIds[y][x];

				if (!AbstractDto.isInternalReadOnlyField(id)) {
					// TODO this position y, 2*x+1 depends on the current layout of the form..
					final Widget widgetValue = table.getWidget(y, 2 * x + 1);
					final Object value;

					if (widgetValue instanceof TextBox) {
						value = ((TextBox) widgetValue).getText();
					} else if (widgetValue instanceof DateBox) {
						value = ((DateBox) widgetValue).getValue();
					} else if (widgetValue instanceof RelateWidget) {
						value = ((RelateWidget) widgetValue).getId();
					} else if (widgetValue instanceof CheckBox) {
						value = ((CheckBox) widgetValue).getValue();
					} else if (widgetValue instanceof TextArea) {
						value = ((TextArea) widgetValue).getText();
					// TODO also enable this for MarkWidget
					// } else if (widgetValue instanceof MarkWidget) {
					// value = widgetValue.
					} else {
						displayError(new RuntimeException("Unexpected Widget: " + widgetValue.getClass())); // unexpected widget
						throw new RuntimeException("Unexpected Widget Type: " + widgetValue.getClass().toString());
					}

					tmpViewable.setFieldValue(id, value);
				}
			}
		}
	}

	protected void save(final FlexTable table, final long id) {
		// id == -1 indicates that there is no id yet.
		// thus if id != -1 we know the id -> we do an update
		final boolean isUpdate = -1 != id && 0 != id;

		LoadIndicator.get().startLoading();

		final AsyncCallback<Void> saveCallback = new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				TabCenterView.instance().get(clazz).saveCompleted();
				LoadIndicator.get().endLoading();
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("could not create");
				LoadIndicator.get().endLoading();
			}
		};

		if (isUpdate) {
			viewable.setId(id);
		}
		setViewable(viewable.getFormFieldIds(), table, viewable);

		if (isUpdate) {
			commonService.update(IANA.mashal(clazz), viewable, id, saveCallback);
		} else {
			commonService.create(IANA.mashal(clazz), viewable, saveCallback);
		}
	}

	protected Label getLabelForField(final int id) {
		return new Label(viewable.getFieldById(id).getLabel());
	}

	/**
	 * Have to provide an instance of ListViewable. Using the instance variable viewable is a special use case..
	 */
	protected Widget getWidgetByType(final AbstractDto tmpViewable, final int fieldId, final View view) {
		return WidgetSelector.getWidgetByType(clazz, tmpViewable, fieldId, view);
	}

	public Class<? extends AbstractDto> getClazz() {
		return clazz;
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
				}
			}
		}
	}

	protected void displayError(final Throwable caught) {
		LoadIndicator.get().endLoading();
		Window.alert(caught.getClass().toString());
		// throw new RuntimeException(caught.getLocalizedMessage());
	}
	
	public enum View {
		DETAIL, EDIT, CREATE
	}
}
