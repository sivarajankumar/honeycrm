package honeycrm.client.field;

import honeycrm.client.DtoRegistry;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.dto.Dto;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;
import honeycrm.client.view.RelateWidget;

import java.io.Serializable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FieldRelate extends AbstractField {
	private static final long serialVersionUID = -1518485985368479493L;
	private String marshalledClazz;

	public FieldRelate() {
	}

	public FieldRelate(final String id, final String clazz, final String label) {
		super(id, label);
		this.marshalledClazz = clazz;
	}

	public String getRelatedClazz() {
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
			final Dto relatedViewable = DtoRegistry.instance.getDto(getRelatedClazz());
			final Hyperlink link = new Hyperlink("", relatedViewable.getHistoryToken() + " " + value);

			Prefetcher.instance.get(new Consumer<Dto>() {
				@Override
				public void setValueAsynch(Dto name) {
					link.setText(name.getQuicksearch());
				}
			}, new ServerCallback<Dto>() {
				@Override
				public void doRpc(final Consumer<Dto> internalCacheCallback) {
					LoadIndicator.get().startLoading();

					ServiceRegistry.commonService().get(getRelatedClazz(), (Long) value, new AsyncCallback<Dto>() {
						@Override
						public void onFailure(Throwable caught) {
							LoadIndicator.get().endLoading();
							Window.alert("Could not get item with id " + value.toString());
						}

						@Override
						public void onSuccess(Dto result) {
							internalCacheCallback.setValueAsynch(result);
							LoadIndicator.get().endLoading();
						}
					});
				}
			}, 60 * 1000, getRelatedClazz(), value);

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
	public Serializable getData(Widget w) {
		return ((RelateWidget) w).getId();
	}
}
