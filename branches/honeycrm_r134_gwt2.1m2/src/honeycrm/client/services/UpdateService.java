package honeycrm.client.services;

import honeycrm.client.dto.Dto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("update")
public interface UpdateService extends RemoteService {
	public void update(Dto dto);
}
