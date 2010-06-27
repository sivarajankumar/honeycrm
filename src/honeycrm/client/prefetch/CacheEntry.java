package honeycrm.client.prefetch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CacheEntry implements Serializable {
	/**
	 * Number of milliseconds the cached values are marked as valid i.e. not out dated.
	 */
	private static final int CACHE_TIME = 3 * 1000;
	private static final long serialVersionUID = 5590996936325404685L;
	private boolean locked;
	private Object value;
	private long timestamp;
	private List<Consumer> callbacks = new LinkedList<Consumer>();

	public CacheEntry() {
	}
	
	public List<Consumer> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<Consumer> callbacks) {
		this.callbacks = callbacks;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		this.timestamp = System.currentTimeMillis();
	}

	public boolean isEmpty() {
		return null == value;
	}
	
	/**
	 * returns true of the cache entry is too old. false otherwise.
	 */
	public boolean isOutOfDate() {
		return System.currentTimeMillis() - timestamp > CACHE_TIME;
	}
}
