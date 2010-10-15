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
	protected Serializable internalGetData(Widget w) {
		return ((CheckBox) w).getValue();
	}

	@Override
	protected Widget editField() {
		return new CheckBox();
	}

	@Override
	protected Widget detailField() {
		return new CheckBox();
	}
}
