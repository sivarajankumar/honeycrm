package honeycrm.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {
	void login(String login, String password, AsyncCallback<Long> callback);
}
