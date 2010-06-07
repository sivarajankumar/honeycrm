package honeycrm.client.dto;

import honeycrm.client.IANA;

public class FieldRelate extends Field {
	private static final long serialVersionUID = -1518485985368479493L;
	private int marshalledClazz;

	public FieldRelate() {
	}

	public FieldRelate(final int id, final Class<? extends AbstractDto> clazz, final String label) {
		super(id, Type.RELATE, label);
		this.marshalledClazz = IANA.mashal(clazz);
	}

	public int getRelatedClazz() {
		return marshalledClazz;
	}
}
