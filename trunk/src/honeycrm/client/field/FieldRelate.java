package honeycrm.client.field;

import honeycrm.client.DtoRegistry;
import honeycrm.client.IANA;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.PrefetcherCallback;
import honeycrm.client.view.RelateWidget;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FieldRelate extends AbstractField {
	private static final long serialVersionUID = -1518485985368479493L;
	private int marshalledClazz;

	public FieldRelate() {
	}

	public FieldRelate(final int id, final Class<? extends AbstractDto> clazz, final String label) {
		super(id, label);
		this.marshalledClazz = IANA.mashal(clazz);
	}

	public int getRelatedClazz() {
		return marshalledClazz;
	}

	@Override
	protected Widget internalGetCreateWidget(Object value) {
		return new RelateWidget(getRelatedClazz(), 0);
	}

	@Override
	protected Widget internalGetDetailWidget(final Object value) {
		if (0 == (Long) value) {
			// return an empty label because no account has been selected yet
			return new Label();
		} else {
			// resolve the real name of the entity by its id and display a HyperLink as
			// widget
			final AbstractDto relatedViewable = DtoRegistry.instance.getDto(IANA.unmarshal(getRelatedClazz()));
			final Hyperlink link = new Hyperlink("", relatedViewable.getHistoryToken() + " " + value);
			Prefetcher.instance.get(getRelatedClazz(), (Long) value, new PrefetcherCallback() {
				@Override
				public void setValueDeferred(String name) {
					link.setText(name);
				}
			});
			
			return link;
		}
	}

	@Override
	protected Widget internalGetEditWidget(Object value) {
		return new RelateWidget(getRelatedClazz(), (null == value) ? 0 : (Long) value);
	}

	@Override
	protected Widget internalGetListWidget(Object value) {
		return internalGetDetailWidget(value);
	}

	@Override
	public Object getData(Widget w) {
		return ((RelateWidget) w).getId();
	}
}
