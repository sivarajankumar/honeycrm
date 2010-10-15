package honeycrm.client.services;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("delete")
public interface DeleteService extends RemoteService {
	public void delete(String kind, long id);
	public void deleteAll(String kind, Set<Long> ids);
	public void deleteAll(String kind);
	public void deleteAllItems();
}
