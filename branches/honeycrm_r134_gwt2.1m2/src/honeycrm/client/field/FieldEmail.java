package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.StringAbbreviation;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldEmail extends AbstractField {
	private static final long serialVersionUID = -1035707556737860138L;

	public FieldEmail() {
	}

	public FieldEmail(final String index, final String label) {
		super(index, label);
	}

	@Override
	protected Widget internalGetCreateWidget(final Object value) {
		final TextBox widget3 = new TextBox();
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Serializable value = dto.get(fieldId);
		if (null == value || value.toString().isEmpty()) {
			return new Label();
		} else {
			// Display email as a mailto:a@b.com link to make sure the clients mail client will
			// be opened.
			final Anchor mailtoLink = new Anchor(StringAbbreviation.shorten(value.toString(), 10), true, "mailto:" + value.toString());;
			mailtoLink.setTitle(value.toString());
			return mailtoLink;
		}
	}

	@Override
	protected Widget internalGetEditWidget(final Object value) {
		final TextBox widget3 = new TextBox();
		widget3.setValue((null == value) ? "" : value.toString());
		return widget3;
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		return ((Anchor) w).getHTML();
	}
}
