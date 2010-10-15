package honeycrm.client.services;

import honeycrm.client.dto.Configuration;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigServiceAsync {
	void getConfiguration(AsyncCallback<Configuration> callback);
}
