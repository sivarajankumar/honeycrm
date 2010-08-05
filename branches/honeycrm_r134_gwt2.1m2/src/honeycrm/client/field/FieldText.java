package honeycrm.client.field;

import honeycrm.client.dto.Dto;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class FieldText extends AbstractField {
	private static final long serialVersionUID = 5081155630980085278L;

	public FieldText() {
	}

	public FieldText(final String index, final String label) {
		super(index, label);
		this.width = 200;
	}

	@Override
	protected Widget internalGetCreateWidget(final Object value) {
		// Display normal TextArea instead of more advanced RichTextArea until a nice
		// RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
		final TextArea widget4 = new TextArea();
		return widget4;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Serializable value = dto.get(fieldId);
		return new Label((null == value) ? "" : value.toString());
	}

	@Override
	protected Widget internalGetEditWidget(final Object value) {
		// Display normal TextArea instead of more advanced RichTextArea until a nice
		// RichTextArea widget is available. The GWT RichTextArea widget has no toolbar...
		final TextArea widget4 = new TextArea();
		widget4.setText((null == value) ? "" : value.toString());
		return widget4;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		return ((TextArea) w).getText();
	}
}
