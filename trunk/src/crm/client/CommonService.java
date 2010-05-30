package crm.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

@RemoteServiceRelativePath("common")
public interface CommonService extends RemoteService {
	// create
	public long create(int dtoIndex, AbstractDto viewable);

	// read
	public AbstractDto get(int dtoIndex, long id);
	public AbstractDto getByName(int dtoIndex, String name);
	public ListQueryResult<? extends AbstractDto> getAll(final int dtoIndex, int from, int to);
	public ListQueryResult<? extends AbstractDto> getAllMarked(final int dtoIndex, int from, int to);
	public ListQueryResult<? extends AbstractDto> getAllByNamePrefix(final int dtoIndex, String prefix, int from, int to);
	public ListQueryResult<? extends AbstractDto> search(int dtoIndex, AbstractDto searchContact, int from, int to);
	public ListQueryResult<? extends AbstractDto> fulltextSearch(String query, int from, int to);

	// update
	public void update(int dtoIndex, AbstractDto account, long id);
	public void mark(int dtoIndex, long id, boolean marked);

	// delete
	public void delete(int dtoIndex, long id);
	public void deleteAll(int dtoIndex, Set<Long> ids);
	public void deleteAll(int dtoIndex);
	public void deleteAllItems();
	public void addDemo(int dtoIndex);
}
