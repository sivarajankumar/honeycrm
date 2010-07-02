package honeycrm.client.prefetch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CacheEntry implements Serializable {
	/**
	 * Number of milliseconds the cached values are marked as valid i.e. not out dated.
	 */
	private static final int DEFAULT_TIMEOUT = 30 * 1000;
	private static final long serialVersionUID = 5590996936325404685L;
	private boolean locked;
	private Object value;
	private long timestamp;
	private long timeout;
	private boolean valid;
	private List<Consumer> callbacks = new LinkedList<Consumer>();

	public CacheEntry() {
		this.timeout = DEFAULT_TIMEOUT;
	}
	
	public CacheEntry(long timeout) {
		this.timeout = timeout;
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
		
		this.valid = true;
		this.timestamp = System.currentTimeMillis();
	}

	public boolean isEmpty() {
		return null == value;
	}
	
	/**
	 * returns true of the cache entry is too old. false otherwise.
	 */
	public boolean isOutOfDate() {
		return System.currentTimeMillis() - timestamp > timeout;
	}

	public boolean isValid() {
		return valid;
	}

	public void makeInvalid() {
		this.valid = false;
	}
}
