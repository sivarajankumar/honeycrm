package honeycrm.client.field;

import honeycrm.client.dto.Dto;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.user.cellview.client.Column;

public class FieldString extends AbstractField<String> {
	private static final long serialVersionUID = -3150226939473188904L;

	public FieldString() {
	}

	public FieldString(final String index, final String label) {
		super(index, label, "");
	}
	
	public FieldString(final String index, final String label, final String defaultValue) {
		super(index, label, defaultValue);
	}

	@Override
	public Column<Dto, String> getColumn(final String fieldName) {
		return new Column<Dto, String>(new EditTextCell()) {
			@Override
			public String getValue(Dto object) {
				return String.valueOf(object.get(fieldName));
			}
		};
	}
}
