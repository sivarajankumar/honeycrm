package honeycrm.client.services;

import java.util.Map;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("read")
public interface ReadService extends RemoteService {
	public ListQueryResult getAll(final String kind);
	public Dto get(String dtoIndex, long id);
	public Dto getByName(String dtoIndex, String name);
	public ListQueryResult getAllAssignedTo(final String dtoIndex, final long employeeID, int from, int to);
	public ListQueryResult getAllMarked(final String dtoIndex, int from, int to);
	public ListQueryResult getAllByNamePrefix(final String dtoIndex, String prefix, int from, int to);
	public ListQueryResult search(String dtoIndex, Dto searchContact, int from, int to);
	public ListQueryResult fulltextSearch(String query, int from, int to);
	public ListQueryResult getAllRelated(String originating, Long id, String related);
	public Map<String, ListQueryResult> getAllRelated(final Long id, final String relatedDtoIndex);
	public ListQueryResult fulltextSearchForModule(final String dtoIndex, String query, int from, int to);
}
