package honeycrm.client.services;

import honeycrm.client.dto.Dto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("create")
public interface CreateService extends RemoteService {
	public long create(Dto dto);
}
