package honeycrm.client.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.profiling.ServiceCallStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommonServiceAsync {
	void getAll(final String dtoIndex, int from, int to, AsyncCallback<ListQueryResult> callback);

	void get(String dtoIndex, long id, AsyncCallback<Dto> callback);

	void search(String dtoIndex, Dto searchContact, int from, int to, AsyncCallback<ListQueryResult> callback);

	void update(Dto account, long id, AsyncCallback<Void> callback);

	void delete(String dtoIndex, long id, AsyncCallback<Void> callback);

	void deleteAll(String dtoIndex, Set<Long> ids, AsyncCallback<Void> callback);

	void addDemo(String dtoIndex, AsyncCallback<Void> callback);

	void getAllByNamePrefix(String dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult> callback);

	void getByName(String dtoIndex, String name, AsyncCallback<Dto> callback);

	void fulltextSearch(String query, int from, int to, AsyncCallback<ListQueryResult> callback);

	void mark(String dtoIndex, long id, boolean marked, AsyncCallback<Void> callback);

	void getAllMarked(String dtoIndex, int from, int to, AsyncCallback<ListQueryResult> callback);

	void deleteAll(String dtoIndex, AsyncCallback<Void> callback);

	void create(Dto viewable, AsyncCallback<Long> callback);

	void deleteAllItems(AsyncCallback<Void> callback);

	void fulltextSearchForModule(String dtoIndex, String query, int from, int to, AsyncCallback<ListQueryResult> callback);

	void feedback(String message, AsyncCallback<Void> callback);

	void getAnnuallyOfferingVolumes(AsyncCallback<Map<Integer, Double>> callback);

	void getDtoConfiguration(AsyncCallback<Map<String, ModuleDto>> callback);

	void importCSV(String module, List<Dto> dtos, AsyncCallback<Void> callback);

	void getServiceCallStatistics(AsyncCallback<Collection<ServiceCallStatistics>> callback);

	void bulkCreate(AsyncCallback<Void> callback);

	void bulkRead(AsyncCallback<Void> callback);

	void getRelationships(AsyncCallback<Map<String, Map<String, Set<String>>>> callback);

	void getAllRelated(Long id, String relatedDtoIndex, AsyncCallback<Map<String, ListQueryResult>> callback);

	void getAllAssignedTo(String dtoIndex, long employeeID, int from, int to, AsyncCallback<ListQueryResult> callback);
}
