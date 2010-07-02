package honeycrm.client.field;

import java.io.Serializable;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class FieldInteger extends AbstractField {
	private static final long serialVersionUID = 8025061226197879958L;

	public FieldInteger() {
	}

	public FieldInteger(final String index, final String label) {
		super(index, label);
	}

	public FieldInteger(String indexQuantity, String string, String string2, int i) {
		super(indexQuantity, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox widget6 = new TextBox();
		widget6.setText(getDefaultValue());
		widget6.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget6;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		final Label widget1 = new Label(value.toString());
		widget1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		return widget1;
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		TextBox widget6 = new TextBox();
		widget6.setText(value.toString());
		widget6.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget6;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Serializable getData(Widget w) {
		return ((TextBox) w).getText();
	}
}
