package honeycrm.client.test;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.profiling.ServiceCallStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CommonServiceTestHelper implements CommonServiceAsync {

	@Override
	public void addDemo(String dtoIndex, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String dtoIndex, long id, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(String dtoIndex, Set<Long> ids, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(String dtoIndex, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllItems(AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void feedback(String message, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(String dtoIndex, long id, AsyncCallback<Dto> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllByNamePrefix(String dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllMarked(String dtoIndex, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllRelated(String originatingDtoIndex, Long id, String relatedDtoIndex, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAnnuallyOfferingVolumes(AsyncCallback<Map<Integer, Double>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getByName(String dtoIndex, String name, AsyncCallback<Dto> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mark(String dtoIndex, long id, boolean marked, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void search(String dtoIndex, Dto searchContact, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wakeupServer(AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void create(Dto viewable, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Dto account, long id, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fulltextSearch(String query, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fulltextSearchForModule(String dtoIndex, String query, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAll(String dtoIndex, int from, int to, AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getDtoConfiguration(AsyncCallback<Map<String, ModuleDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importCSV(String module, List<Dto> dtos, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getServiceCallStatistics(AsyncCallback<Collection<ServiceCallStatistics>> callback) {
		// TODO Auto-generated method stub
		
	}

}
