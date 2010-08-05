package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.NumberParser;

import java.io.Serializable;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class FieldInteger extends AbstractField {
	private static final int DEFAULT_WIDTH = 70;
	private static final long serialVersionUID = 8025061226197879958L;

	public FieldInteger() {
	}

	public FieldInteger(final String index, final String label) {
		super(index, label);
		this.width = DEFAULT_WIDTH;
	}

	public FieldInteger(final String index, final String label, final int defaultValue) {
		super(index, label, String.valueOf(defaultValue));
		this.width = DEFAULT_WIDTH;
	}

	public FieldInteger(final String indexQuantity, final String string, final String string2, final int i) {
		super(indexQuantity, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(final Object value) {
		final TextBox widget6 = new TextBox();
		widget6.setText(getDefaultValue());
		widget6.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget6;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Label widget1 = new Label(String.valueOf(NumberParser.convertToInteger(dto.get(fieldId))));
		widget1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		return widget1;
	}

	@Override
	protected Widget internalGetEditWidget(final Object value) {
		final TextBox widget6 = new TextBox();
		widget6.setText(String.valueOf(NumberParser.convertToInteger(value)));
		widget6.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget6;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	public Serializable getData(final Widget w) {
		final String value = (String) super.getData(w);
		return NumberParser.convertToInteger(value);
	}

	@Override
	public Serializable getTypedData(final Object value) {
		return NumberParser.convertToInteger(value);
	}
}
