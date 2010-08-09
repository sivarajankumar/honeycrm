package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
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

	// private IField iField;

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

	/*
	 * public AbstractField(String name, String label2, IField iField) { this(name, label2); this.iField = iField; }
	 */

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
	public String getWidthString() {
		return width + "px";
	}

	public Widget getWidget(final View view, final Dto dto, final String fieldId) {
		switch (view) {
		case DETAIL:
			return decorateWidget(internalGetDetailWidget(dto, (fieldId)));
		case EDIT:
			return decorateWidget(internalGetEditWidget(dto.get(fieldId)));
		case LIST:
			return decorateWidget(internalGetListWidget(dto, (fieldId)));
		case CREATE:
			return decorateWidget(internalGetCreateWidget(dto.get(fieldId)));
		case LIST_HEADER:
			return getHeaderWidget(dto.get(fieldId));
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

	protected Widget internalGetCreateWidget(Object value) {
		return setData23(editField(), defaultValue, View.CREATE);
	}

	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		return setData23(detailField(), dto.get(fieldId), View.DETAIL); // TODO do this in the upper class only
	}

	protected Widget internalGetEditWidget(Object value) {
		return setData23(editField(), (Serializable) value, View.EDIT); // TODO change interface
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
	private Label getHeaderWidget(final Object value) {
		return (Label) decorateWidget(new Label(getLabel()));
	}

	/**
	 * Adjust the width of the widget and the enable / disable it depending on the field settings.
	 */
	private Widget decorateWidget(final Widget widget) {
		if (hasSuggestedWidth()) {
			widget.setWidth(getWidthString());
		}
		return widget;
	}

	protected Widget setData23(final Widget widget, final Serializable value, final View view) {
		if (widget instanceof Label) {
			((Label) widget).setText(formattedValue(value, view));
		} else if (widget instanceof TextBox) {
			((TextBox) widget).setText(formattedValue(value, view));
		} else if (widget instanceof TextArea) {
			((TextArea) widget).setText(formattedValue(value, view));
		} else if (widget instanceof CheckBox) {
			((CheckBox) widget).setValue((Boolean) value);
		} else if (widget instanceof Anchor) {
			// TODO do this only for value != null
			// TOOD use Label when value == null
			widget.setTitle(stringify(value));
			((Anchor) widget).setText(formattedValue(value, view));
			((Anchor) widget).setHref("mailto:" + String.valueOf(value));
		} else if (widget instanceof DateBox) {
			if (value instanceof Date) {
				((DateBox) widget).setValue((Date) value);
			} else if (value instanceof String && String.valueOf(value).equals("")) {
				// nothing to do
			} else {
				Window.alert("Cannot set date on DateBox for value: " + String.valueOf(value));
			}
		} else {
			Window.alert("Cannot handle widget " + widget.getClass());
			throw new RuntimeException("Cannot handle widget " + widget.getClass());
		}
		return decorate23(widget, View.DETAIL == view);
	}

	private Widget decorate23(Widget widget, final boolean readOnly) {
		if (readOnly && widget instanceof FocusWidget) {
			((FocusWidget) widget).setEnabled(false);
		}
		return widget;
	}

	private String formattedValue(final Serializable value, final View view) {
		return stringify(internalFormattedValue(value));
	}

	/**
	 * Subclasses may override this and implement their own formatting for their content.
	 */
	protected String internalFormattedValue(final Serializable value) {
		return stringify(value);
	}

	protected String stringify(Serializable value) {
		return null == value ? "" : String.valueOf(value);
	}
}