package honeycrm.client.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.s.SortDirection;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReadServiceAsync {
	void getAll(String kind, final int from, final int to, AsyncCallback<ListQueryResult> callback);
	void getAll(String kind, String sortColumn, SortDirection sortDirection, final int from, final int to, AsyncCallback<ListQueryResult> callback);
	void get(String dtoIndex, long id, AsyncCallback<Dto> callback);
	void getByName(String dtoIndex, String name, AsyncCallback<Dto> callback);
	void getAllAssignedTo(String dtoIndex, long employeeID, int from, int to, AsyncCallback<ListQueryResult> callback);
	void getAllMarked(String dtoIndex, int from, int to, AsyncCallback<ListQueryResult> callback);
	void getAllByNamePrefix(String dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult> callback);
	void search(String dtoIndex, Dto searchContact, int from, int to, AsyncCallback<ListQueryResult> callback);
	void fulltextSearch(String query, int from, int to, AsyncCallback<ListQueryResult> callback);
	void getAllRelated(String originating, Long id, String related, AsyncCallback<ListQueryResult> callback);
	void getAllRelated(Long id, String relatedDtoIndex, AsyncCallback<Map<String, ListQueryResult>> callback);
	void fulltextSearchForModule(String dtoIndex, String query, int from, int to, AsyncCallback<ListQueryResult> callback);
	void getAllAssignedTo(long employeeID, int from, int to, AsyncCallback<HashMap<String, ListQueryResult>> callback);
}
