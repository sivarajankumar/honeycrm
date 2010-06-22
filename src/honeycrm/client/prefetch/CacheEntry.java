package honeycrm.client.prefetch;

import honeycrm.client.dto.AbstractDto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

// TODO add timestamp and invalidate this entry when timestamp is too old
public class CacheEntry implements Serializable {
	private static final long serialVersionUID = 5590996936325404685L;
	private AbstractDto dto;
	private boolean locked;
	private List<PrefetcherCallback> callbacks = new LinkedList<PrefetcherCallback>();

	public CacheEntry() {

	}

	public AbstractDto getDto() {
		return dto;
	}

	public void setDto(AbstractDto dto) {
		this.dto = dto;
	}

	public List<PrefetcherCallback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<PrefetcherCallback> callbacks) {
		this.callbacks = callbacks;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public boolean isEmpty() {
		return null == dto;
	}
}
