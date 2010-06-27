package honeycrm.client.prefetch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

// TODO add timestamp and invalidate this entry when timestamp is too old
public class CacheEntry implements Serializable {
	private static final long serialVersionUID = 5590996936325404685L;
	private boolean locked;
	private Object value;
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
	}

	public boolean isEmpty() {
		return null == value;
	}
}
