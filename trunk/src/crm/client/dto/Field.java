package crm.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Field implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	protected int id;
	protected Type type;
	protected String label;

	public Field() { // for gwt
	}

	public Field(final int id, final Type type, final String label) {
		this.id = id;
		this.type = type;
		this.label = label;
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

	public enum Type {
		STRING, TEXT, DATE, BOOLEAN, RELATE, CURRENCY, INTEGER, EMAIL, ENUM, MULTIENUM
	}
}