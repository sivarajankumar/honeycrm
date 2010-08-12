package honeycrm.client.field;

import honeycrm.client.view.AbstractView.View;

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
	protected void internalSetData(DateBox widget, Date value, View view) {
		if (null == value) {
			// nothing to do
		} else {
			(widget).setValue(value);
		}
	}

	@Override
	protected void internalSetData(Label widget, Serializable value, View view) {
		widget.setText(internalFormattedValue(value));
	}
	
	@Override
	public String internalFormattedValue(Serializable value) {
		return null == value ? "" : DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format((Date) value);
	}
}
