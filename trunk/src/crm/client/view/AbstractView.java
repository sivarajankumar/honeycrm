package crm.client.view;

import java.util.Date;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
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
import crm.client.dto.DtoAccount;

abstract public class AbstractView extends Composite {
	// private static final NumberFormat DATE_FORMAT = DateFo NumberFormat.getFormat(LocaleInfo.getCurrentLocale().getNumberConstants().currencyPattern());
	private static final NumberFormat CURRENCY_FORMAT_RW = NumberFormat.getFormat("0.00", "EUR"); // TODO how to prefix euro sign? or other unicode characters?
	private static final NumberFormat CURRENCY_FORMAT_RO = NumberFormat.getFormat("0.00"); // TODO how to prefix euro sign? or other unicode characters?
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
		final boolean isUpdate = -1 != id;

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
	protected Widget getWidgetByType(final AbstractDto tmpViewable, final int fieldId, final boolean readOnly) {
		if (AbstractDto.INDEX_MARKED == fieldId) {
			return new MarkWidget(clazz, tmpViewable);
		}

		final Object value = tmpViewable.getFieldValue(fieldId);

		if (readOnly) {
			// TODO display related entity instead of value.toString if type is RELATE
			switch (tmpViewable.getFieldById(fieldId).getType()) {
			case RELATE:
				if (0 == (Long) value) {
					// return an empty label because no account has been selected yet
					return new Label();
				} else {
					// resolve the real name of the entity by its id and display a HyperLink as widget
					final AbstractDto relatedViewable = new DtoAccount();
					final Hyperlink link = new Hyperlink("", relatedViewable.getHistoryToken() + " " + value);

					commonService.get(IANA.mashal(DtoAccount.class), (Long) value, new AsyncCallback<AbstractDto>() {
						@Override
						public void onFailure(Throwable caught) {
							// assume no related entity has been selected
							// Window.alert("Could not find account with id " + value);
							link.setText("Not Found");
						}

						@Override
						public void onSuccess(AbstractDto result) {
							if (null == result) {
								// assume no related entity has been selected
								// Window.alert("Could not find account with id " + value);
								link.setText("");
							} else {
								// TODO make DTO independent
								link.setText(((DtoAccount) result).getName());
							}
						}
					});
					return link;
				}
			case BOOLEAN:
				CheckBox widget7 = new CheckBox();
				widget7.setEnabled(false);
				widget7.setValue((Boolean) value);
				return widget7;
			case CURRENCY:
				Label widget5 = new Label();
				widget5.setText(CURRENCY_FORMAT_RO.format((Double) value));
				return widget5;
			case EMAIL:
				if (value.toString().isEmpty()) {
					return new Label();
				} else {
					// Display email as a mailto:a@b.com link to make sure the clients mail client will be opened.
					return new Anchor(value.toString(), true, "mailto:" + value.toString());
				}
			case TEXT:
				return new Label((null == value) ? "" : value.toString());
			default:
				return new Label((null == value) ? "" : value.toString());
			}
		} else {
			switch (tmpViewable.getFieldById(fieldId).getType()) {
			case BOOLEAN:
				CheckBox widget = new CheckBox();
				widget.setValue((Boolean) value);
				return widget;
			case DATE:
				DateBox widget2 = new DateBox();
				widget2.setValue((Date) value);
				return widget2;
			case INTEGER:
				TextBox widget6 = new TextBox();
				widget6.setText(Long.toString((Long) value));
				return widget6;
			case EMAIL:
			case STRING:
				TextBox widget3 = new TextBox();
				widget3.setValue((String) value);
				return widget3;
			case TEXT:
				// Display normal TextArea instead of more advanced RichTextArea until a nice RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
				TextArea widget4 = new TextArea();
				widget4.setText((String) value);
				return widget4;
			case RELATE:
				return new RelateWidget(DtoAccount.class, (Long) value);
			case CURRENCY:
				TextBox widget5 = new TextBox();
				widget5.setText(CURRENCY_FORMAT_RW.format((Double) value));
				return widget5;
			default:
				throw new RuntimeException("Unexpected Type: " + tmpViewable.getFieldById(fieldId).getType().toString()); // should never reach this point
			}
		}
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
}
