package honeycrm.client.services;

import honeycrm.client.dto.Dto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CreateServiceAsync {
	void create(Dto dto, AsyncCallback<Long> callback);
}
