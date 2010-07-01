package honeycrm.client.field;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class FieldText extends AbstractField {
	private static final long serialVersionUID = 5081155630980085278L;

	public FieldText() {
	}

	public FieldText(final int index, final String label) {
		super(index, label);
		this.width = 200;
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		// Display normal TextArea instead of more advanced RichTextArea until a nice
		// RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
		TextArea widget4 = new TextArea();
		return widget4;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return new Label((null == value) ? "" : value.toString());
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		// Display normal TextArea instead of more advanced RichTextArea until a nice
		// RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
		TextArea widget4 = new TextArea();
		widget4.setText((null == value) ? "" : value.toString());
		return widget4;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Object getData(Widget w) {
		return ((TextArea) w).getText();
	}
}
