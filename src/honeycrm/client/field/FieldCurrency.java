package honeycrm.client.field;

import honeycrm.client.misc.NumberParser;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class FieldCurrency extends AbstractField {
	private static final long serialVersionUID = -8253981703594953379L;

	public FieldCurrency() {
	}

	// TODO find a way to inherit the constructors from the parent class to avoid declaring them here again
	public FieldCurrency(final int index, final String label) {
		super(index, label);
	}

	public FieldCurrency(final int index, final String label, final String defaultValue) {
		super(index, label, defaultValue);
	}

	public FieldCurrency(int indexSum, String string, String string2, int i, boolean b) {
		super(indexSum, string, string2, i, b);
	}

	public FieldCurrency(int indexDiscount, String string, String string2, int i) {
		super(indexDiscount, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox widget3 = new TextBox();
		widget3.setText(getDefaultValue());
		widget3.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		Label w = new Label();
		// use a currency constant defined in
		// com/google/gwt/i18n/client/constants/CurrencyCodeMapConstants.properties which can be found
		// in gwt-user.jar
		w.setText(NumberFormat.getCurrencyFormat("EUR").format(NumberParser.convertToDouble(value)));
		w.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		return w;
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		TextBox widget5 = new TextBox();
		widget5.setText(NumberFormat.getFormat("0.00").format((Double) value));
		widget5.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return widget5;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

}
