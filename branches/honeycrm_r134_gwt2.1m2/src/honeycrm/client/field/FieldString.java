package honeycrm.client.field;

import honeycrm.client.dto.Dto;

import java.io.Serializable;

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

	public FieldString(final String indexName, final String string, final String string2, final int i) {
		super(indexName, string, string2, i);
	}

	@Override
	protected Widget internalGetCreateWidget(final Object value) {
		final TextBox widget3 = new TextBox();
		return widget3;
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		final Serializable value = dto.get(fieldId);
		return new Label((null == value) ? "" : value.toString());
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
}
