package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.view.ITableWidget;
import honeycrm.client.view.ServiceTableWidget;
import honeycrm.client.view.AbstractView.View;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public class FieldTable extends AbstractField {
	private static final long serialVersionUID = 5834729592030900010L;

	public FieldTable() {
	}

	public FieldTable(final String id, final String label) {
		super(id, label);
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		return getTableWidget(value, View.CREATE);
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return getTableWidget(value, View.DETAIL);
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		return getTableWidget(value, View.EDIT);
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	/**
	 * Create the widget and initialize it with data.
	 */
	private Widget getTableWidget(Object value, final View view) {
		final ITableWidget w = new ServiceTableWidget(view);
		w.setData((List<Dto>) value);
		return w;
	}

	@Override
	public Object getData(Widget w) {
		return ((ITableWidget) w).getData();
	}
}
