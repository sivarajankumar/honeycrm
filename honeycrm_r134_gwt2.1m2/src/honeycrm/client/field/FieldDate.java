package honeycrm.client.field;

import honeycrm.client.misc.View;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FieldDate extends AbstractField {
	private static final long serialVersionUID = 734488177370075237L;

	public FieldDate() {
	}

	public FieldDate(final String index, final String label) {
		super(index, label);
	}

	@Override
	protected Serializable internalGetData(Widget w) {
		return ((DateBox) w).getValue();
	}

	@Override
	protected Widget editField() {
		final DateBox box = new DateBox();
		box.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG)));
		return box;
	}

	@Override
	protected void internalSetData(DateBox widget, Object value, View view) {
		if (value instanceof Date) {
			widget.setValue((Date) value);
		} else {
			// nothing to do
		}
	}

	@Override
	protected void internalSetData(Label widget, Object value, View view) {
		widget.setText(internalFormattedValue(value));
	}
	
	@Override
	public String internalFormattedValue(Object value) {
		return null == value ? "" : DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format((Date) value);
	}
}
