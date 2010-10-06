package honeycrm.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeleteServiceAsync {
	void delete(String kind, long id, AsyncCallback<Void> callback);
}
