package honeycrm.client.field;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FieldDate extends AbstractField {
	private static final long serialVersionUID = 734488177370075237L;

	public FieldDate() {
	}

	public FieldDate(final String index, final String label) {
		super(index, label);
	}
/*
	@Override
	protected Widget internalGetCreateWidget(Object value) {
	    DateBox dateBox = new DateBox();
		dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG)));
	    return dateBox;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Serializable value = dto.get(fieldId);
		if (null == value) {
			return new Label();
		} else {
			return new Label(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format((Date) value));
		}
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		DateBox widget2 = new DateBox();
		widget2.setValue((Date) value);
		widget2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG)));
		return widget2;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}
*/
	@Override
	protected Serializable internalGetData(Widget w) {
		return ((DateBox) w).getValue();
	}
	
	@Override
	protected Widget editField() {
		return new DateBox();
	}
}
