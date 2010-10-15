package honeycrm.client.field;

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

	public FieldInteger(final String index, final String label, int defaultValue) {
		super(index, label, String.valueOf(defaultValue));
		this.width = DEFAULT_WIDTH;
	}

	@Override
	public Serializable getData(Widget w) {
		final String value = (String) super.getData(w);
		return NumberParser.convertToInteger(value);
	}

	@Override
	public Serializable getTypedData(Object value) {
		return NumberParser.convertToInteger(value);
	}
	
	@Override
	protected Widget editField() {
		final TextBox box = new TextBox();
		box.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return box;
	}
	
	@Override
	protected Widget detailField() {
		final Label label = new Label();
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		return label;
	}
}
