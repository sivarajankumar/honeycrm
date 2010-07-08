package honeycrm.client.field;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.view.MarkWidget;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;

public class FieldMark extends AbstractField {
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

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		final Dto dto = DtoModuleRegistry.instance().get(dtoIndex).createDto();
		// dto.setId(NumberParser.convertToLong(value));
		dto.setId(dtoId);
		dto.setMarked((Boolean) value);

		return new MarkWidget(dto);
	}

	@Override
	protected Widget internalGetDetailWidget(Object value) {
		return internalGetCreateWidget(value);
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		return internalGetCreateWidget(value);
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetCreateWidget(value);
	};

	@Override
	public Serializable getData(Widget w) {
		throw new RuntimeException("Not Supported");
	}
}
