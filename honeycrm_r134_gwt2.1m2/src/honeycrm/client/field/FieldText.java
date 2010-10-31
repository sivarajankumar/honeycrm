package honeycrm.client.field;

import java.io.Serializable;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class FieldText extends AbstractField {
	private static final long serialVersionUID = 5081155630980085278L;

	public FieldText() {
	}

	public FieldText(final String index, final String label, final int width) {
		super(index, label);
		this.width = width;
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		return ((TextArea) w).getText();
	}

	@Override
	protected Widget editField() {
		return new TextArea();
	}
}
