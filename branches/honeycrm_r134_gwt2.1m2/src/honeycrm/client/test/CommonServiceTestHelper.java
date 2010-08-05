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
	public void addDemo(final String dtoIndex, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final String dtoIndex, final long id, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(final String dtoIndex, final Set<Long> ids, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(final String dtoIndex, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllItems(final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void feedback(final String message, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(final String dtoIndex, final long id, final AsyncCallback<Dto> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllByNamePrefix(final String dtoIndex, final String prefix, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllMarked(final String dtoIndex, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAnnuallyOfferingVolumes(final AsyncCallback<Map<Integer, Double>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getByName(final String dtoIndex, final String name, final AsyncCallback<Dto> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mark(final String dtoIndex, final long id, final boolean marked, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void search(final String dtoIndex, final Dto searchContact, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wakeupServer(final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void create(final Dto viewable, final AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final Dto account, final long id, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fulltextSearch(final String query, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fulltextSearchForModule(final String dtoIndex, final String query, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAll(final String dtoIndex, final int from, final int to, final AsyncCallback<ListQueryResult> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getDtoConfiguration(final AsyncCallback<Map<String, ModuleDto>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void importCSV(final String module, final List<Dto> dtos, final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getServiceCallStatistics(final AsyncCallback<Collection<ServiceCallStatistics>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bulkCreate(final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bulkRead(final AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRelationships(final AsyncCallback<Map<String, Map<String, Set<String>>>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllRelated(final Long id, final String relatedDtoIndex, final AsyncCallback<Map<String, ListQueryResult>> callback) {
		// TODO Auto-generated method stub

	}

}
