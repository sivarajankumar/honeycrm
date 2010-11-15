package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.View;

import java.io.Serializable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

// TODO this should be done by field currency somehow.. the fields should provide a "String format(Serializable value);" method.
abstract public class AbstractField implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Proposed width of the widget in px that will be used to render this field. Or 0 if nothing has been proposed.
	 */
	protected int width = 0;
	/**
	 * Suggested default value for this field.
	 */
	protected String defaultValue = "";
	/**
	 * Dto-wide unique id used to identify the property stored in the dto class that relates to this field.
	 */
	protected String id;
	/**
	 * The name of the field which is used as a label in list views, create forms, etc.
	 */
	protected String label;

	public AbstractField() { // for gwt
	}

	public AbstractField(final String id, final String label) {
		this.id = id;
		this.label = label;
	}

	public AbstractField(String id, String label, String defaultValue) {
		this(id, label);
		this.defaultValue = defaultValue;
	}

	/**
	 * Returns true if there is a width value that has been suggested for the widget used for rendering this field.
	 */
	public boolean hasSuggestedWidth() {
		return 0 != width;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Return the width suggested for the widget used to render this field as a string, e.g., "100px"
	 */
	private String getWidthString() {
		return width + "px";
	}

	public Widget getWidget(final View view, final Dto dto, final String fieldId) {
		switch (view) {
		case DETAIL:
			return internalGetDetailWidget(dto, (fieldId));
		case EDIT:
			return internalGetEditWidget(dto, (fieldId));
		case LIST:
			return internalGetListWidget(dto, (fieldId));
		case CREATE:
			return internalGetCreateWidget(dto, (fieldId));
		case LIST_HEADER:
			return getHeaderWidget();
		default:
			throw new RuntimeException("Unknown view: " + view);
		}
	}

	protected Widget editField() {
		return new TextBox();
	}

	protected Widget detailField() {
		return new Label();
	}

	protected Widget internalGetCreateWidget(final Dto dto, final String fieldId) {
		return decorateWidget(setData(editField(), defaultValue, View.CREATE), false);
	}

	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		return decorateWidget(setData(detailField(), dto.get(fieldId), View.DETAIL), true); // TODO do this in the upper class only
	}

	protected Widget internalGetEditWidget(final Dto dto, final String fieldId) {
		return decorateWidget(setData(editField(), dto.get(fieldId), View.EDIT), false); // TODO change interface
	}

	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId); // TODO remove method completely
	}

	/**
	 * Sub classes may override this method to handle the default widgets differently (e.g. additional conversion).
	 */
	public Serializable getData(final Widget w) {
		if (w instanceof Label) {
			return ((Label) w).getText();
		} else if (w instanceof TextBox) {
			return ((TextBox) w).getText();
		} else {
			try {
				return internalGetData(w);
			} catch (RuntimeException e) {
				// open a dialog and re-throw exception
				Window.alert("Unexpected type: " + w.getClass());
				throw e;
			}
		}
	}

	/**
	 * Sub classes may override this method to handle other widget types.
	 */
	protected Serializable internalGetData(final Widget w) {
		Window.alert("Unexpected type: " + w.getClass());
		throw new RuntimeException("Unexpected type: " + w.getClass());
	}

	public Serializable getTypedData(final Object value) {
		return null == value ? "" : value.toString();
	}

	/**
	 * This returns a label containing the title of this field, all other properties (e.g., width, alignment) are set as for a normal content field. TODO this method should not receive a value as parameter since it does not need it
	 */
	private Label getHeaderWidget() {
		return (Label) decorateWidget(new Label(getLabel()), true);
	}

	private Widget setData(final Widget widget, final Object value, final View view) {
		if (widget instanceof HTML) {
			internalSetData((HTML) widget, value, view);
		} else if (widget instanceof Label) {
			internalSetData((Label) widget, value, view);
		} else if (widget instanceof TextBox) {
			internalSetData((TextBox) widget, value, view);
		} else if (widget instanceof TextArea) {
			internalSetData((TextArea) widget, value, view);
		} else if (widget instanceof CheckBox) {
			internalSetData((CheckBox) widget, value, view);
		} else if (widget instanceof DateBox) {
			internalSetData((DateBox) widget, value, view);
		} else if (widget instanceof Anchor) {
			internalSetData((Anchor) widget, value, view);
		} else if (widget instanceof ListBox) {
			internalSetData((ListBox) widget, value, view);
		} else {
			Window.alert("Cannot handle widget " + widget.getClass());
			throw new RuntimeException("Cannot handle widget " + widget.getClass());
		}
		return widget;
	}

	/**
	 * Adjust the width of the widget and the enable / disable it depending on the field settings.
	 */
	private Widget decorateWidget(Widget widget, final boolean readOnly) {
		if (readOnly && widget instanceof FocusWidget) {
			((FocusWidget) widget).setEnabled(false);
		}
		if (hasSuggestedWidth()) {
			widget.setWidth(getWidthString());
		}
		return widget;
	}

	private String formattedValue(final Object value, final View view) {
		return stringify(internalFormattedValue(value));
	}

	/**
	 * Subclasses may override this and implement their own formatting for their content.
	 */
	public String internalFormattedValue(final Object value) {
		return stringify(value);
	}

	protected String stringify(Object value) {
		return null == value ? "" : String.valueOf(value);
	}

	/**
	 * Subclasses may override this class to initialize values on the widgets they use.
	 */
	protected void internalSetData(Anchor widget, Object value, View view) {
		notImplemented();
	}
	
	protected void internalSetData(DateBox widget, Object value, View view) {
		notImplemented();
	}

	protected void internalSetData(CheckBox widget, Object value, View view) {
		if (value instanceof Boolean) {
			widget.setValue((Boolean) value);
		} else if (value instanceof String) {
			widget.setValue(value.toString().equals("true"));
		}
	}

	protected void internalSetData(HTML widget, Object value, View view) {
		notImplemented();
	}
	
	protected void internalSetData(Label widget, Object value, View view) {
		widget.setText(formattedValue(value, view));
	}

	protected void internalSetData(TextBox widget, Object value, View view) {
		widget.setText(formattedValue(value, view));
	}

	protected void internalSetData(TextArea widget, Object value, View view) {
		widget.setText(formattedValue(value, view));
	}

	protected void internalSetData(ListBox widget, Object value, View view) {
		notImplemented();
	}
	
	private void notImplemented() {
		Window.alert("not implemented");
		throw new RuntimeException("not implemented");
	}
}