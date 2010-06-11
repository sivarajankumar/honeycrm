package honeycrm.client.field;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldEmail extends AbstractField {
	private static final long serialVersionUID = -1035707556737860138L;

	public FieldEmail() {
	}
	
	public FieldEmail(final int index, final String label) {
		super(index, label);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox widget3 = new TextBox();
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		if (null == value || value.toString().isEmpty()) {
			return new Label();
		} else {
			// Display email as a mailto:a@b.com link to make sure the clients mail client will
			// be opened.
			return new Anchor(value.toString(), true, "mailto:" + value.toString());
		}
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		TextBox widget3 = new TextBox();
		widget3.setValue((null == value) ? "" : value.toString());
		return widget3;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}
}
