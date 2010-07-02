package honeycrm.client.field;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldString extends AbstractField {
	private static final long serialVersionUID = -3150226939473188904L;

	public FieldString() {
	}

	public FieldString(final String index, final String label) {
		super(index, label);
	}

	public FieldString(String indexName, String string, String string2, int i) {
		super(indexName, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		TextBox widget3 = new TextBox();
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return new Label((null == value) ? "" : value.toString());
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		TextBox widget3 = new TextBox();
		widget3.setValue((null == value) ? "" : value.toString());
		return widget3;
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Object getData(Widget w) {
		return ((TextBox) w).getText();
	}
}
