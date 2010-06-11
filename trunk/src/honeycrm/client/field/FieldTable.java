package honeycrm.client.field;

import honeycrm.client.view.ServiceTableWidget;
import honeycrm.client.view.AbstractView.View;

import com.google.gwt.user.client.ui.Widget;

public class FieldTable extends AbstractField {
	private static final long serialVersionUID = 5834729592030900010L;

	public FieldTable() {
	}

	public FieldTable(final int id, final String label) {
		super(id, label);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		return new ServiceTableWidget(View.CREATE);
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return new ServiceTableWidget(View.DETAIL);
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		return new ServiceTableWidget(View.EDIT);
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

}
