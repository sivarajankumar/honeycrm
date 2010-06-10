package honeycrm.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FieldEnum extends Field implements IsSerializable, Serializable {
	private static final long serialVersionUID = -4542742508636055819L;
	protected String[] options;

	public FieldEnum() { // for gwt
	}

	public FieldEnum(final int id, final String label, final String... options) {
		super(id, Type.ENUM, label);
		this.options = options;
	}

	public String[] getOptions() {
		return options;
	}
}
