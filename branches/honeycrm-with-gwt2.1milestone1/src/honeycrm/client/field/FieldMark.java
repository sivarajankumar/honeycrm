package honeycrm.client.field;

import honeycrm.client.DtoRegistry;
import honeycrm.client.IANA;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.view.MarkWidget;

import com.google.gwt.user.client.ui.Widget;

public class FieldMark extends AbstractField {
	private static final long serialVersionUID = 2910540849623881202L;
	private int dtoIndex;
	private long dtoId;

	public FieldMark() {
	}
	
	public FieldMark(final int index, final String label, final int dtoIndex, final long id) {
		super(index, label);
		this.dtoIndex = dtoIndex;
		this.dtoId = id;
	}
	
	@Override
	protected Widget internalGetCreateWidget(Object value) {
		final AbstractDto dto = DtoRegistry.instance.getDto(IANA.unmarshal(dtoIndex));
		// dto.setId(NumberParser.convertToLong(value));
		dto.setId(dtoId);
		
		return new MarkWidget(dto.getClass(), dto);
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
	public Object getData(Widget w) {
		throw new RuntimeException("Not Supported");
	}
}
