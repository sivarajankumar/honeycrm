package honeycrm.client;

import honeycrm.client.dto.AbstractDto;
import honeycrm.client.dto.ListQueryResult;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface CommonServiceAsync {
	void getAll(final int dtoIndex, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void get(int dtoIndex, long id, AsyncCallback<AbstractDto> callback);
	void search(int dtoIndex, AbstractDto searchContact, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void update(int dtoIndex, AbstractDto account, long id, AsyncCallback<Void> callback);
	void delete(int dtoIndex, long id, AsyncCallback<Void> callback);
	void deleteAll(int dtoIndex, Set<Long> ids, AsyncCallback<Void> callback);
	void addDemo(int dtoIndex, AsyncCallback<Void> callback);
	void getAllByNamePrefix(int dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void getByName(int dtoIndex, String name, AsyncCallback<AbstractDto> callback);
	void fulltextSearch(String query, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void mark(int dtoIndex, long id, boolean marked, AsyncCallback<Void> callback);
	void getAllMarked(int dtoIndex, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void deleteAll(int dtoIndex, AsyncCallback<Void> callback);
	void create(int dtoIndex, AbstractDto viewable, AsyncCallback<Long> callback);
	void deleteAllItems(AsyncCallback<Void> callback);
	void wakeupServer(AsyncCallback<Void> callback);
	void getAllRelated(int originatingDtoIndex, Long id, int relatedDtoIndex, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
	void fulltextSearchForModule(int dtoIndex, String query, int from, int to, AsyncCallback<ListQueryResult<? extends AbstractDto>> callback);
}
