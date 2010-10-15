package honeycrm.client.services;

import honeycrm.client.dto.Dto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UpdateServiceAsync {
	void update(Dto dto, AsyncCallback<Void> callback);
}
