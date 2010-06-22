package honeycrm.client.prefetch;

import honeycrm.client.IANA;
import honeycrm.client.LoadIndicator;
import honeycrm.client.ServiceRegistry;
import honeycrm.client.dto.AbstractDto;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class Prefetcher {
	public static final Prefetcher instance = new Prefetcher();
	private Map<Class<? extends AbstractDto>, Map<Long, CacheEntry>> cache = new HashMap<Class<? extends AbstractDto>, Map<Long, CacheEntry>>();

	private Prefetcher() {
	}
	
	public void get(final int marshalledClazz, final Long id, final PrefetcherCallback callback) {
		final Class<? extends AbstractDto> clazz = IANA.unmarshal(marshalledClazz);

		if (!cache.containsKey(clazz)) {
			cache.put(clazz, new HashMap<Long, CacheEntry>());
		}

		final CacheEntry entry;

		if (cache.get(clazz).containsKey(id)) {
			entry = cache.get(clazz).get(id);
		} else {
			cache.get(clazz).put(id, entry = new CacheEntry());
		}

		entry.getCallbacks().add(callback);
		
		if (!entry.isLocked()) {
			if (entry.isEmpty()) {
				// entry is not locked but is still empty.
				// so we are the first who want to access the field. lock the entry and request data from server.

				getDataFromServer(marshalledClazz, id, entry);
			} else {
				// entry is not locked and not empty
				// just tell the client what the cached value is.
				callback.setValueDeferred(entry.getDto().getQuicksearchItem());
			}
		}
	}

	private void getDataFromServer(final int marshalledClazz, final Long id, final CacheEntry entry) {
		entry.setLocked(true);

		LoadIndicator.get().startLoading();
		ServiceRegistry.commonService().get(marshalledClazz, id, new AsyncCallback<AbstractDto>() {
			@Override
			public void onFailure(Throwable caught) {
				LoadIndicator.get().endLoading();
				entry.setLocked(false);
			}

			@Override
			public void onSuccess(AbstractDto result) {
				LoadIndicator.get().endLoading();
				entry.setLocked(false);
				entry.setDto(result);

				for (final PrefetcherCallback callback: entry.getCallbacks()) {
					if (null == result) {
						callback.setValueDeferred("Not found");
					} else {
						callback.setValueDeferred(result.getQuicksearchItem());
					}
				}
				entry.getCallbacks().clear();
			}
		});
	}
}
