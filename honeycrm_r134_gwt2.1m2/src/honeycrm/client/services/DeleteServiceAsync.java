package honeycrm.client.services;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeleteServiceAsync {
	void delete(String kind, long id, AsyncCallback<Void> callback);
	void deleteAll(String kind, Set<Long> ids, AsyncCallback<Void> callback);
	void deleteAll(String kind, AsyncCallback<Void> callback);
	void deleteAllItems(AsyncCallback<Void> callback);
}
