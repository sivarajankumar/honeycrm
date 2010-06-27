package honeycrm.client.field;

import honeycrm.client.DtoRegistry;
import honeycrm.client.IANA;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.ServerCallback;
import honeycrm.client.view.RelateWidget;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

			Prefetcher.instance.get(new Consumer<AbstractDto>() {
				@Override
				public void setValueAsynch(AbstractDto name) {
					link.setText(((AbstractDto) name).getQuicksearchItem());
				}
			}, new ServerCallback<AbstractDto>() {
				@Override
				public void doRpc(final Consumer<AbstractDto> internalCacheCallback) {
					LoadIndicator.get().startLoading();

					ServiceRegistry.commonService().get(getRelatedClazz(), (Long) value, new AsyncCallback<AbstractDto>() {
						@Override
						public void onFailure(Throwable caught) {
							LoadIndicator.get().endLoading();
							Window.alert("Could not get item with id " + value.toString());
						}

						@Override
						public void onSuccess(AbstractDto result) {
							internalCacheCallback.setValueAsynch(result);
							LoadIndicator.get().endLoading();
						}
					});
				}
			}, getRelatedClazz(), value);

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
