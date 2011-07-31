package honeycrm.client.services;

import honeycrm.client.misc.ServiceCallStatistics;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommonServiceAsync {
	void feedback(String message, AsyncCallback<Void> callback);
	void getServiceCallStatistics(AsyncCallback<Collection<ServiceCallStatistics>> callback);
	void bulkCreate(AsyncCallback<Void> callback);
	void bulkRead(AsyncCallback<Void> callback);
}
