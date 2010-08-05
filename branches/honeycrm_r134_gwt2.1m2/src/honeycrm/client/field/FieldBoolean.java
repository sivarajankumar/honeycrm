package honeycrm.client.field;

import honeycrm.client.dto.Dto;

import java.io.Serializable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldBoolean extends AbstractField {
	private static final long serialVersionUID = -4437595172130002836L;

	public FieldBoolean() {
	}

	public FieldBoolean(final String index, final String label) {
		super(index, label);
	}

	@Override
	protected Widget internalGetCreateWidget(final Object value) {
		final CheckBox widget = new CheckBox();
		return widget;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final CheckBox widget7 = new CheckBox();
		widget7.setValue((Boolean) dto.get(fieldId));
		widget7.setEnabled(false);
		return widget7;
	}

	@Override
	protected Widget internalGetEditWidget(final Object value) {
		final CheckBox widget = new CheckBox();
		widget.setValue((Boolean) value);
		return widget;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		return ((CheckBox) w).getValue();
	}
}
