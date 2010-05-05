package crm.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

@RemoteServiceRelativePath("common")
public interface CommonService extends RemoteService {
	// create
	public void create(int dtoIndex, Viewable viewable);

	// read
	public Viewable get(int dtoIndex, long id);
	public Viewable getByName(int dtoIndex, String name);
	public ListQueryResult<? extends Viewable> getAll(final int dtoIndex, int from, int to);
	public ListQueryResult<? extends Viewable> getAllByNamePrefix(final int dtoIndex, String prefix, int from, int to);
	public ListQueryResult<? extends Viewable> search(int dtoIndex, Viewable searchContact, int from, int to);
	
	// update
	public void update(int dtoIndex, Viewable account, long id);

	// delete
	public void delete(int dtoIndex, long id);
	public void deleteAll(int dtoIndex, Set<Long> ids);
	public void addDemo(int dtoIndex);
}
