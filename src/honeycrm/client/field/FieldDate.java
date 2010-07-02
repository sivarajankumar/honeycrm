package honeycrm.client.field;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
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
	protected Widget internalGetCreateWidget(Object value) {
		DateBox widget3 = new DateBox();
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		if (null == value) {
			return new Label();
		} else {
			return new Label(DateTimeFormat.getMediumDateFormat().format((Date) value));
		}
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		DateBox widget2 = new DateBox();
		widget2.setValue((Date) value);
		return widget2;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Serializable getData(Widget w) {
		return ((DateBox) w).getValue();
	}
}
