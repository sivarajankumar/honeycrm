package honeycrm.client.field;

import honeycrm.client.misc.StringAbbreviation;
import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

public class FieldEmail extends AbstractField {
	private static final long serialVersionUID = -1035707556737860138L;

	public FieldEmail() {
	}

	public FieldEmail(final String index, final String label) {
		super(index, label);
	}

	@Override
	protected Serializable internalGetData(Widget w) {
		return ((Anchor) w).getHTML();
	}

	@Override
	public String internalFormattedValue(Serializable value) {
		return StringAbbreviation.shorten(stringify(value), 10);
	}

	@Override
	protected void internalSetData(Anchor widget, Serializable value, View view) {
		// TODO do this only for value != null
		// TODO use Label when value == null
		widget.setTitle(stringify(value));
		(widget).setText(internalFormattedValue(value));
		(widget).setHref("mailto:" + String.valueOf(value));
	}

	@Override
	protected Widget detailField() {
		return new Anchor();
	}
}
