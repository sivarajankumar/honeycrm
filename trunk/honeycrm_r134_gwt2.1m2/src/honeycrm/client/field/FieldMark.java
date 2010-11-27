package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.misc.Callback;
import honeycrm.client.misc.View;
import honeycrm.client.view.MarkWidget;

import java.io.Serializable;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Widget;

public class FieldMark extends AbstractField<Boolean, Void> {
	private static final long serialVersionUID = 2910540849623881202L;
	private String dtoIndex;
	private long dtoId;

	public FieldMark() {
	}

	public FieldMark(final String index, final String label, final String dtoIndex, final long id) {
		super(index, label);
		this.dtoIndex = dtoIndex;
		this.dtoId = id;
	}
/*
	@Override
	protected Widget internalGetCreateWidget(Object value) {
		final Dto dto = DtoModuleRegistry.instance().get(dtoIndex).createDto();
		// dto.setId(NumberParser.convertToLong(value));
		dto.setId(dtoId);
		dto.setMarked((Boolean) value);

		return new MarkWidget(dto);
	}

	@Override
	protected Widget internalGetDetailWidget(final Dto dto, final String fieldId) {
		return internalGetCreateWidget(dto.get(fieldId));
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		return internalGetCreateWidget(value);
	}

	@Override
	protected Widget internalGetListWidget(final Dto dto, final String fieldId) {
		return internalGetCreateWidget(dto.get(fieldId));
	};
*/
	@Override
	public Serializable getData(Widget w) {
		throw new RuntimeException("Not Supported");
	}
	
	@Override
	protected Widget editField() {
		return detailField();
	}
	
	@Override
	protected Widget detailField() {
		// TODO this is more difficult
		final boolean value = false;
		final Dto dto = DtoModuleRegistry.instance().get(dtoIndex).createDto();
		// dto.setId(NumberParser.convertToLong(value));
		dto.setId(dtoId);
		dto.setMarked(value);

		return new MarkWidget(dto, null);
	}

	@Override
	public Column<Dto, Boolean> getColumn(String fieldName, final View viewMode, final Callback<Void> fieldUpdatedCallback) {
		return new Column<Dto, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(Dto object) {
				return null;
			}
		};
	}
}
