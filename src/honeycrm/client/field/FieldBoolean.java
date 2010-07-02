package honeycrm.client.field;

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
	protected Widget internalGetCreateWidget(Object value) {
		CheckBox widget = new CheckBox();
		return widget;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		CheckBox widget7 = new CheckBox();
		widget7.setValue((Boolean) value);
		widget7.setEnabled(false);
		return widget7;
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		CheckBox widget = new CheckBox();
		widget.setValue((Boolean) value);
		return widget;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Serializable getData(Widget widget) {
		return ((CheckBox) widget).getValue();
	}
}
