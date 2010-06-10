package honeycrm.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Field implements IsSerializable, Serializable {
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
	protected int id;
	protected Type type;
	/**
	 * The name of the field which is used as a label in list views, create forms, etc.
	 */
	protected String label;

	public Field() { // for gwt
	}

	public Field(final int id, final Type type, final String label, final String defaultValue, final int width, final boolean readOnly) {
		this(id, type, label, defaultValue, width);
		this.readOnly = readOnly;
	}
	
	public Field(final int id, final Type type, final String label, final String defaultValue, final int width) {
		this(id, type, label, defaultValue);
		this.width = width;
	}
	
	public Field(final int id, final Type type, final String label, final String defaultValue) {
		this.id = id;
		this.type = type;
		this.label = label;
		this.defaultValue = defaultValue;
	}

	public Field(final int id, final Type type, final String label) {
		this(id, type, label, "");
	}
	
	/**
	 * Returns true if there is a width value that has been suggested for the widget used for rendering this field.
	 */
	public boolean hasSuggestedWidth() {
		return 0 != width;
	}

	public int getId() {
		return id;
	}

	public Type getType() {
		return type;
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

	public enum Type {
		STRING, TEXT, DATE, BOOLEAN, RELATE, CURRENCY, INTEGER, EMAIL, ENUM, MULTIENUM, TABLE
	}
}