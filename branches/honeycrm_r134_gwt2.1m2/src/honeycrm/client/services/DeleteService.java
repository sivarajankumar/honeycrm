package honeycrm.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("delete")
public interface DeleteService extends RemoteService {
	public void delete(String kind, long id);
}
