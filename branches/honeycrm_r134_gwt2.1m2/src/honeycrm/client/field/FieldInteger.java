package honeycrm.client.field;

import honeycrm.client.misc.NumberParser;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;

public class FieldInteger extends AbstractField {
	private static final int DEFAULT_WIDTH = 70;
	private static final long serialVersionUID = 8025061226197879958L;

	public FieldInteger() {
	}

	public FieldInteger(final String index, final String label, int defaultValue) {
		super(index, label, String.valueOf(defaultValue));
		this.width = DEFAULT_WIDTH;
	}
/*
	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox widget6 = new TextBox();
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
	protected Widget internalGetEditWidget(Object value) {
		TextBox widget6 = new TextBox();
		widget6.setText(String.valueOf(NumberParser.convertToInteger(value)));
		widget6.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget6;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}*/

	@Override
	public Serializable getData(Widget w) {
		final String value = (String) super.getData(w);
		return NumberParser.convertToInteger(value);
	}

	@Override
	public Serializable getTypedData(Object value) {
		return NumberParser.convertToInteger(value);
	}
}
