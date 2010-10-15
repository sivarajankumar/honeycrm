package honeycrm.client.field;

import honeycrm.client.misc.NumberParser;
import honeycrm.client.view.AbstractView.View;

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
	private static final int DEFAULT_WIDTH = 70;
	private static final long serialVersionUID = -8253981703594953379L;

	public FieldCurrency() {
		this.width = DEFAULT_WIDTH;
	}

	public FieldCurrency(final String index, final String label, final String defaultValue) {
		super(index, label, defaultValue);
		this.width = DEFAULT_WIDTH;
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
	public Serializable getData(Widget w) {
		final String value = (String) super.getData(w);

		try {
			return NumberFormat.getCurrencyFormat("EUR").parse(value);
		} catch (NumberFormatException e) {
			// Gwt throw a number format exception.
			return NumberParser.convertToDouble(value);
		}
	}

	@Override
	protected void internalSetData(Label widget, Object value, View view) {
		(widget).setText(formatRead().format(NumberParser.convertToDouble(value)));
		(widget).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	}

	@Override
	protected void internalSetData(TextBox widget, Object value, View view) {
		(widget).setText(formatRead().format(NumberParser.convertToDouble((value))));
		(widget).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		addEvents(widget);
	}

	// create and return the number format instances within these methods instead of creating them on object instantiation to avoid serialisation problems
	// gwt will not be able to serialize this object if number format instances have to be created when the default constructor () is called.
	private NumberFormat formatWrite() {
		return NumberFormat.getFormat("0.00");
	}

	private NumberFormat formatRead() {
		return NumberFormat.getCurrencyFormat("EUR");
	}

	@Override
	public Serializable getTypedData(Object value) {
		return NumberParser.convertToDouble(value);
	}
	
	@Override
	public String internalFormattedValue(Object value) {
		return formatRead().format(NumberParser.convertToDouble(value));
	}
}
