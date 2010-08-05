package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.view.AbstractView.View;
import honeycrm.client.view.ITableWidget;
import honeycrm.client.view.ServiceTableWidget;

import java.io.Serializable;
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
	protected Widget internalGetCreateWidget(final Object value) {
		return getTableWidget(value, View.CREATE);
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		return getTableWidget(dto.get(fieldId), View.DETAIL);
	}

	@Override
	protected Widget internalGetEditWidget(final Object value) {
		return getTableWidget(value, View.EDIT);
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	/**
	 * Create the widget and initialize it with data.
	 */
	private Widget getTableWidget(final Object value, final View view) {
		final ITableWidget w = new ServiceTableWidget(view);
		w.setData((List<Dto>) value);
		return w;
	}

	@Override
	protected Serializable internalGetData(final Widget w) {
		return (Serializable) ((ITableWidget) w).getData();
	}
}
