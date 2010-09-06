package honeycrm.server.domain;

import com.google.appengine.api.datastore.Key;

/**
 * Interface used for all persistable objects
 */
public interface Bean {
	void setId(Key id);
	Key getId();
}