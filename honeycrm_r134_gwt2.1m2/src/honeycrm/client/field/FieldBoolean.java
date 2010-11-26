package honeycrm.client.field;

import honeycrm.client.dto.Dto;

import java.io.Serializable;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldBoolean extends AbstractField<Boolean> {
	private static final long serialVersionUID = -4437595172130002836L;

	public FieldBoolean() {
	}

	public FieldBoolean(final String index, final String label, final boolean defaultValue) {
		super(index, label, String.valueOf(defaultValue));
	}

	@Override
	protected Serializable internalGetData(Widget w) {
		return ((CheckBox) w).getValue();
	}

	@Override
	protected Widget editField() {
		return new CheckBox();
	}

	@Override
	public Column<Dto, Boolean> getColumn(String fieldName) {
		return new Column<Dto, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(Dto object) {
				return null;
			}
		};
	}

	@Override
	protected Widget detailField() {
		return new CheckBox();
	}
}
