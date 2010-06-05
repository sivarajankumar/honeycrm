package crm.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FieldMultiEnum extends FieldEnum implements IsSerializable, Serializable {
	private static final long serialVersionUID = 7371457456391185007L;

	// Separator string sequence between multiple selected enumeration string values.
	// A selection of the values A and B by the user will be marshalled to "A"+ SEPARATOR + "B"
	// and unmarshalled by splitting the stored string on every occurence of the separator string.
	// NOTE: using ^ as separator is a bad idea because in String.split() it will be interpreted as
	// a regular expression.
	public static final String SEPARATOR = "__23__";

	public FieldMultiEnum() {
	}

	public FieldMultiEnum(final int id, final String label, final String... options) {
		super(id, label, options);
		type = Type.MULTIENUM;
	}
}
