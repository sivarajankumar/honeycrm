package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.misc.View;
import honeycrm.client.view.ITableWidget;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Widget;

public class FieldTable extends AbstractField<String> {
	private static final long serialVersionUID = 5834729592030900010L;

	public FieldTable() {
	}

	public FieldTable(final String id, final String label) {
		super(id, label);
	}

	@Override
	protected Widget internalGetCreateWidget(final Dto dto, final String fieldId) {
		return getTableWidget(dto, fieldId, View.CREATE);
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		return getTableWidget(dto, (fieldId), View.DETAIL);
	}

	@Override
	protected Widget internalGetEditWidget(final Dto dto, final String fieldId) {
		return getTableWidget(dto, fieldId, View.EDIT);
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetDetailWidget(dto, fieldId);
	}

	/**
	 * Create the widget and initialize it with data.
	 */
	private Widget getTableWidget(final Dto dto, final String fieldId, final View view) {
		// TODO use common ITableView interface instead
		// TODO make this independent of the widget / chose the widget
		final Serializable value = dto.get(fieldId);

		final honeycrm.client.ServiceTableView v = new honeycrm.client.ServiceTableView();
		final honeycrm.client.ServiceTablePresenter p = new honeycrm.client.ServiceTablePresenter(v, view, dto.getModule(), fieldId);
		v.setPresenter(p);
		p.setValue((ArrayList<Dto>) value);

		return v;
	}

	@Override
	protected Serializable internalGetData(Widget w) {
		// TODO use presenter.getValue() instead
		return (Serializable) ((ITableWidget) w).getData();
	}

	@Override
	public Column<Dto, String> getColumn(String fieldName, final View viewMode) {
		return null;
	}
}
