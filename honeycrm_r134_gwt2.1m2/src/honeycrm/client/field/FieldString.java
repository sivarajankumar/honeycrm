package honeycrm.client.field;

public class FieldString extends AbstractField {
	private static final long serialVersionUID = -3150226939473188904L;

	public FieldString() {
	}

	public FieldString(final String index, final String label) {
		super(index, label);
		defaultValue = "";
	}
}
