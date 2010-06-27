package honeycrm.client.prefetch;

import java.util.HashMap;
import java.util.Map;

public class Prefetcher {
	public static final Prefetcher instance = new Prefetcher();
	private Map<CacheKey, CacheEntry> cache = new HashMap<CacheKey, CacheEntry>();
	private long hits = 0;
	private long misses = 0;
	
	private Prefetcher() {
	}

	public <T> void get(final Consumer<T> callback, final ServerCallback<T> serverCallback, final Object...parameters) {
		final CacheKey key = new CacheKey(parameters);

		if (!cache.containsKey(key)) {
			cache.put(key, new CacheEntry());
		}

		final CacheEntry entry = cache.get(key);

		if (entry.isLocked()) {
			// this entry is currently retrieved from the server.
			// do nothing just add the client to the callbacks for this cache entry.
			entry.getCallbacks().add(callback);
		} else {
			if (entry.isEmpty()) {
				// entry is not locked but is still empty.
				// so we are the first who want to access the field. lock the entry and request data from server.
				misses++;

				entry.setLocked(true);
				
				serverCallback.doRpc(new Consumer<T>() {
					@Override
					public void setValueAsynch(T result) {
						entry.setValue(result);
						entry.setLocked(false);
						
						// call all clients back that registered while this item has been locked
						for (final Consumer<T> currentPrefetcherCallback: entry.getCallbacks()) {
							currentPrefetcherCallback.setValueAsynch(result);
						}
						
						entry.getCallbacks().clear();
					}
				});
			} else {
				// entry is not locked and not empty
				// just tell the client what the cached value is.
				callback.setValueAsynch((T) entry.getValue());
				hits++;
			}
		}
	}
}
