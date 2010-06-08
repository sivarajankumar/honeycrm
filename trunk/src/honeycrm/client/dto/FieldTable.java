package honeycrm.client.dto;

public class FieldTable extends Field {
	private static final long serialVersionUID = 5834729592030900010L;

	public FieldTable() {
	}
	
	public FieldTable(final int id, final String label) {
		super(id, Field.Type.TABLE, label);
	}
}
