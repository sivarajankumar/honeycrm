package honeycrm.client.field;

import honeycrm.client.view.AbstractView.View;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

public class FieldWebsite extends AbstractField {
	private static final long serialVersionUID = -3830838265148832910L;

	public FieldWebsite() {
	}

	public FieldWebsite(final String index, final String label) {
		super(index, label);
		defaultValue = "http://";
	}

	@Override
	protected Widget detailField() {
		return new Anchor();
	}

	@Override
	protected void internalSetData(Anchor widget, Serializable value, View view) {
		widget.setHref(withHttp(stringify(value)));
		widget.setText(stringify(value));
	}
	
	private String withHttp(final String website) {
		return website.startsWith("http://") ? website : "http://" + website;
	}
}
