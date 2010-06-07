package honeycrm.client.test;

import honeycrm.client.CommonServiceAsync;
import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.ListQueryResult;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class CommonServiceTestHelper implements CommonServiceAsync {
	@Override
	public void addDemo(int dtoIndex, AsyncCallback<Void> callback) {
		
	}

	@Override
	public void create(int dtoIndex, AbstractDto viewable, AsyncCallback<Long> callback) {
		
	}

	@Override
	public void delete(int dtoIndex, long id, AsyncCallback<Void> callback) {
		
	}

	@Override
	public void deleteAll(int dtoIndex, Set<Long> ids, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(int dtoIndex, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllItems(AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fulltextSearch(String query, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(int dtoIndex, long id, AsyncCallback<AbstractDto> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAll(int dtoIndex, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllByNamePrefix(int dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllMarked(int dtoIndex, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllRelated(int originatingDtoIndex, Long id, int relatedDtoIndex, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByName(int dtoIndex, String name, AsyncCallback<AbstractDto> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mark(int dtoIndex, long id, boolean marked, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void search(int dtoIndex, AbstractDto searchContact, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int dtoIndex, AbstractDto account, long id, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeupServer(AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub
		
	}

}
