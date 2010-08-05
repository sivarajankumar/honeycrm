package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractField implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Is this a read only field or not?
	 */
	protected boolean readOnly = false;
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

	public AbstractField(final String id, final String label, final String defaultValue, final int width, final boolean readOnly) {
		this(id, label, defaultValue, width);
		this.readOnly = readOnly;
	}

	public AbstractField(final String id, final String label, final String defaultValue, final int width) {
		this(id, label, defaultValue);
		this.width = width;
	}

	public AbstractField(final String id, final String label, final String defaultValue) {
		this.id = id;
		this.label = label;
		this.defaultValue = defaultValue;
	}

	public AbstractField(final String id, final String label) {
		this(id, label, "");
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

	public boolean isReadOnly() {
		return readOnly;
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

	abstract protected Widget internalGetListWidget(final Dto dto, final String fieldId);

	abstract protected Widget internalGetDetailWidget(final Dto dto, final String fieldId);

	abstract protected Widget internalGetCreateWidget(final Object value);

	abstract protected Widget internalGetEditWidget(final Object value);

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
			} catch (final RuntimeException e) {
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
	 * This returns a label containing the title of this field, all other properties (e.g., width, alignment) are set as for a normal content field.
	 * TODO this method should not receive a value as parameter since it does not need it
	 */
	private Label getHeaderWidget(final Object value) {
		return (Label) decorateWidget(new Label(getLabel()));

		// final Label label = (Label) decorateWidget(internalGetDetailWidget(value));
		// label.setText(getLabel());
		// return label;
	}

	/**
	 * Adjust the width of the widget and the enable / disable it depending on the field settings.
	 */
	private Widget decorateWidget(final Widget widget) {
		if (hasSuggestedWidth()) {
			widget.setWidth(getWidthString());
		}
		/*
		 * if (widget instanceof FocusWidget) { // TODO why is this necessary again?
		 * 
		 * // only do this if widget is still enabled. this prevents enabling checkboxes being rw after they have been disabled in getDetailWidget. if (((FocusWidget)widget).isEnabled()) { // call set enabled method when ever possible // enable this field if it is not readonly. disable the field if it is readonly. ((FocusWidget) widget).setEnabled(!isReadOnly()); } }
		 */
		return widget;
	}
}