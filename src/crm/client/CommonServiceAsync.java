package crm.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

public interface CommonServiceAsync {
	void getAll(final int dtoIndex, int from, int to, AsyncCallback<ListQueryResult<? extends Viewable>> callback);
	void get(int dtoIndex, long id, AsyncCallback<Viewable> callback);
	void create(int dtoIndex, Viewable viewable, AsyncCallback<Void> callback);
	void search(int dtoIndex, Viewable searchContact, int from, int to, AsyncCallback<ListQueryResult<? extends Viewable>> callback);
	void update(int dtoIndex, Viewable account, long id, AsyncCallback<Void> callback);
	void delete(int dtoIndex, long id, AsyncCallback<Void> callback);
	void deleteAll(int dtoIndex, Set<Long> ids, AsyncCallback<Void> callback);
	void addDemo(int dtoIndex, AsyncCallback<Void> callback);
	void getAllByNamePrefix(int dtoIndex, String prefix, int from, int to, AsyncCallback<ListQueryResult<? extends Viewable>> callback);
	void getByName(int dtoIndex, String name, AsyncCallback<Viewable> callback);
}
