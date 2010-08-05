package honeycrm.client.prefetch;

import java.util.HashMap;
import java.util.Map;

// TODO invalidate the right cache items on every update / create
// TODO limit cache size and apply some deletion policy (e.g. last recently used).
// TODO otherwise cache only ever grows.
public class Prefetcher {
	public static final Prefetcher instance = new Prefetcher();
	private final Map<CacheKey, CacheEntry> cache = new HashMap<CacheKey, CacheEntry>();
	private final PrefetcherStats stats = new PrefetcherStats();

	private Prefetcher() {
	}

	public void invalidate(final Object... parameters) {
		final CacheKey key = new CacheKey(parameters);

		if (cache.containsKey(key)) {
			cache.get(key).makeInvalid();
		}
	}

	public <T> void get(final Consumer<T> callback, final ServerCallback<T> serverCallback, final long timeout, final Object... parameters) {
		final CacheKey key = new CacheKey(parameters);

		if (!cache.containsKey(key)) {
			cache.put(key, new CacheEntry(timeout));
			stats.increaseItemCount();
		}

		final CacheEntry entry = cache.get(key);

		entry.getCallbacks().add(callback);

		if (entry.isLocked()) {
			// this entry is currently retrieved from the server.
			// do nothing just add the client to the callbacks for this cache entry.
		} else {
			if (entry.isEmpty() || !entry.isValid() || entry.isOutOfDate()) {
				// entry is not locked but is still empty.
				// so we are the first who want to access the field. lock the entry and request data from server.
				stats.increaseMisses();

				entry.setLocked(true);

				serverCallback.doRpc(new Consumer<T>() {
					@Override
					public void setValueAsynch(final T result) {
						entry.setValue(result);
						entry.setLocked(false);

						// call all clients back that registered while this item has been locked
						for (final Consumer<T> currentPrefetcherCallback : entry.getCallbacks()) {
							currentPrefetcherCallback.setValueAsynch(result);
						}

						entry.getCallbacks().clear();
					}
				});
			} else {
				// entry is not locked and not empty
				// just tell the client what the cached value is.
				callback.setValueAsynch((T) entry.getValue());
				stats.increaseHits();
			}
		}
	}

	public PrefetcherStats getStats() {
		return stats;
	}

	public class PrefetcherStats {
		private long hits = 0;
		private long misses = 0;
		private long itemCount = 0;

		public void increaseHits() {
			hits++;
		}

		public void increaseItemCount() {
			this.itemCount++;
		}

		public void increaseMisses() {
			misses++;
		}

		public long getItemCount() {
			return itemCount;
		}

		public long getHits() {
			return hits;
		}

		public long getMisses() {
			return misses;
		}
	}
}
