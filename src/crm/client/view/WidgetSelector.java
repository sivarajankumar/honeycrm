package crm.client.view;

import java.util.Date;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import crm.client.CollectionHelper;
import crm.client.CommonServiceAsync;
import crm.client.IANA;
import crm.client.ServiceRegistry;
import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.FieldEnum;
import crm.client.dto.FieldMultiEnum;
import crm.client.dto.FieldRelate;
import crm.client.dto.Field.Type;
import crm.client.view.AbstractView.View;

/**
 * Determines the widget that should be displayed for a given field type.
 */
public class WidgetSelector {
	protected static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getShortDateFormat();
	protected static final NumberFormat CURRENCY_FORMAT_RW = NumberFormat.getFormat("0.00", "EUR"); // TODO how to prefix euro sign? or other unicode characters?
	protected static final NumberFormat CURRENCY_FORMAT_RO = NumberFormat.getFormat("0.00"); // TODO how to prefix euro sign? or other unicode characters?
	protected static final CommonServiceAsync commonService = ServiceRegistry.commonService();

	public static Widget getWidgetByType(final Class<? extends AbstractDto> clazz, final AbstractDto tmpViewable, final int fieldId, final View view) {
		if (AbstractDto.INDEX_MARKED == fieldId) {
			return new MarkWidget(clazz, tmpViewable);
		}

		final Object value = tmpViewable.getFieldValue(fieldId);

		switch (view) {
		case CREATE:
			return getCreateWidget(tmpViewable, fieldId);
		case DETAIL:
			return getDetailWidget(tmpViewable, fieldId, value);
		case EDIT:
			return getEditWidget(tmpViewable, fieldId, value);
		default:
			throw new RuntimeException("Unexpected Type: " + tmpViewable.getFieldById(fieldId).getType().toString()); // should never reach this point
		}
	}

	private static Widget getEditWidget(final AbstractDto tmpViewable, final int fieldId, final Object value) {
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
			widget3.setValue((null == value) ? "" : value.toString());
			return widget3;
		case TEXT:
			// Display normal TextArea instead of more advanced RichTextArea until a nice RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
			TextArea widget4 = new TextArea();
			widget4.setText((null == value) ? "" : value.toString());
			return widget4;
		case RELATE:
			if (tmpViewable.getFieldById(fieldId) instanceof FieldRelate) {
				return new RelateWidget(((FieldRelate)tmpViewable.getFieldById(fieldId)).getClazz(), (Long) value);
			} else {
				throw new RuntimeException("Expected FieldRelate. Received " + tmpViewable.getFieldById(fieldId).getClass().toString());
			}
		case CURRENCY:
			TextBox widget5 = new TextBox();
			widget5.setText(CURRENCY_FORMAT_RW.format((Double) value));
			return widget5;
		case ENUM:
		case MULTIENUM:
			if (tmpViewable.getFieldById(fieldId) instanceof FieldEnum) {
				final Set<String> selectedItems = CollectionHelper.toSet(value.toString().split(FieldMultiEnum.SEPARATOR));
				final String[] options = ((FieldEnum) tmpViewable.getFieldById(fieldId)).getOptions();
				final ListBox box = new ListBox(Type.MULTIENUM == tmpViewable.getFieldById(fieldId).getType());

				for (int i = 0; i < options.length; i++) {
					box.addItem(options[i]);
					if (selectedItems.contains(options[i])) { // preselect the item(s) that have been stored in the db
						box.setItemSelected(i, true);
					}
				}
				
				return box;
			} else {
				throw new RuntimeException("Expected FieldEnum but received something else. Cannot instantiate ListBox.");
			}
		default:
			throw new RuntimeException("Unexpected Type: " + tmpViewable.getFieldById(fieldId).getType().toString()); // should never reach this point
		}
	}

	private static Widget getDetailWidget(final AbstractDto tmpViewable, final int fieldId, final Object value) {
		// TODO display related entity instead of value.toString if type is RELATE
		switch (tmpViewable.getFieldById(fieldId).getType()) {
		case RELATE:
			if (0 == (Long) value) {
				// return an empty label because no account has been selected yet
				return new Label();
			} else {
				if (tmpViewable.getFieldById(fieldId) instanceof FieldRelate) {
				//	return new RelateWidget(((FieldRelate)tmpViewable.getFieldById(fieldId)).getClazz(), 0);
					
				// resolve the real name of the entity by its id and display a HyperLink as widget
				final AbstractDto relatedViewable = new DtoAccount(); // TODO determine history token from field..
				final Hyperlink link = new Hyperlink("", relatedViewable.getHistoryToken() + " " + value);

				commonService.get(((FieldRelate)tmpViewable.getFieldById(fieldId)).getClazz(), (Long) value, new AsyncCallback<AbstractDto>() {
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
							link.setText(result.getQuicksearchItem());
						}
					}
				});
				return link;
				}
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
			if (null == value || value.toString().isEmpty()) {
				return new Label();
			} else {
				// Display email as a mailto:a@b.com link to make sure the clients mail client will be opened.
				return new Anchor(value.toString(), true, "mailto:" + value.toString());
			}
		case DATE:
			if (null == value) {
				return new Label();
			} else {
				return new Label(DATE_FORMAT.format((Date)value));
			}
		case MULTIENUM:
			String ul = "";
			for (final String selection : value.toString().split(FieldMultiEnum.SEPARATOR)) {
				ul += "<li>" + selection + "</li>";
			}
			return new HTML("<ul>" + ul + "</ul>");
		case ENUM:
		case TEXT:
		default:
			return new Label((null == value) ? "" : value.toString());
		}
	}

	private static Widget getCreateWidget(final AbstractDto tmpViewable, final int fieldId) {
		switch (tmpViewable.getFieldById(fieldId).getType()) {
		case BOOLEAN:
			CheckBox widget = new CheckBox();
			return widget;
		case DATE:
			DateBox widget2 = new DateBox();
			return widget2;
		case INTEGER:
			TextBox widget6 = new TextBox();
			return widget6;
		case EMAIL:
		case STRING:
			TextBox widget3 = new TextBox();
			return widget3;
		case TEXT:
			// Display normal TextArea instead of more advanced RichTextArea until a nice RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
			TextArea widget4 = new TextArea();
			return widget4;
		case RELATE:
			if (tmpViewable.getFieldById(fieldId) instanceof FieldRelate) {
				return new RelateWidget(((FieldRelate)tmpViewable.getFieldById(fieldId)).getClazz(), 0);
			} else {
				throw new RuntimeException("Expected FieldRelate. Received " + tmpViewable.getFieldById(fieldId).getClass().toString());
			}
		case CURRENCY:
			TextBox widget5 = new TextBox();
			return widget5;
		case ENUM:
		case MULTIENUM:
			ListBox box = new ListBox(Type.MULTIENUM == tmpViewable.getFieldById(fieldId).getType());
			if (tmpViewable.getFieldById(fieldId) instanceof FieldEnum) {
				final String[] options = ((FieldEnum) tmpViewable.getFieldById(fieldId)).getOptions();
				for (int i = 0; i < options.length; i++) {
					box.addItem(options[i]);
				}
				return box;
			} else {
				throw new RuntimeException("Expected FieldEnum but received something else. Cannot instantiate ListBox.");
			}
		default:
			throw new RuntimeException("Unexpected Type: " + tmpViewable.getFieldById(fieldId).getType().toString()); // should never reach this point
		}
	}
}
