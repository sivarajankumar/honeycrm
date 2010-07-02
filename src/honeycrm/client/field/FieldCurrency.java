package honeycrm.client.field;

import honeycrm.client.misc.NumberParser;

import java.io.Serializable;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
	public FieldCurrency(final String index, final String label) {
		super(index, label);
	}

	public FieldCurrency(final String index, final String label, final String defaultValue) {
		super(index, label, defaultValue);
	}

	public FieldCurrency(String indexSum, String string, String string2, int i, boolean b) {
		super(indexSum, string, string2, i, b);
	}

	public FieldCurrency(String indexDiscount, String string, String string2, int i) {
		super(indexDiscount, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox w = new TextBox();
		w.setText(formatRead().format(NumberParser.convertToDouble((getDefaultValue()))));
		w.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		return addEvents(w);
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		Label w = new Label();
		// use a currency constant defined in
		// com/google/gwt/i18n/client/constants/CurrencyCodeMapConstants.properties which can be found
		// in gwt-user.jar
		w.setText(formatRead().format(NumberParser.convertToDouble(value)));
		w.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		return w;
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		final TextBox w = new TextBox();
		w.setText(formatRead().format(NumberParser.convertToDouble(value)));
		w.setTextAlignment(TextBoxBase.ALIGN_RIGHT);

		return addEvents(w);
	}

	private TextBox addEvents(final TextBox textbox) {
		textbox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				try {
					// user clicked into the field. convert the read format "EUR 1.00" into the write format "1.00"
					textbox.setText(formatWrite().format(formatRead().parse(textbox.getText())));
				} catch (NumberFormatException e) {
				}
			}
		});

		textbox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				try {
					// the user left the field. convert the editing format "1.00" back to the read format "EUR 1.00"
					final String formatted = formatRead().format(formatWrite().parse(textbox.getText()));
					textbox.setText(formatted);
				} catch (NumberFormatException e) {
				}
			}
		});

		return textbox;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Serializable getData(Widget w) {
		try {
			return NumberFormat.getCurrencyFormat("EUR").parse(((TextBox) w).getText());
		} catch (NumberFormatException e) {
			// Gwt throw a number format exception.
			return NumberParser.convertToDouble(((TextBox) w).getText());
		}
	}

	// create and return the number format instances within these methods instead of creating them on object instantiation to avoid serialisation problems
	// gwt will not be able to serialize this object if number format instances have to be created when the default constructor () is called.
	private NumberFormat formatWrite() {
		return NumberFormat.getFormat("0.00");
	}

	private NumberFormat formatRead() {
		return NumberFormat.getCurrencyFormat("EUR");
	}
}
