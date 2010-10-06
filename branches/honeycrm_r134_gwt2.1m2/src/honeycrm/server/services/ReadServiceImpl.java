package honeycrm.server.services;

import java.util.Map;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.services.ReadService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

public class ReadServiceImpl extends NewService implements ReadService {
	private static final long serialVersionUID = 4925571929249643554L;

	// TODO only transmit required fields for listview - do not copy all fields into dto to save bandwidth
	@Override
	public ListQueryResult getAll(String kind, final int from, final int to) {
		final PreparedQuery pq = db.prepare(new Query(kind));
		return copy.entitiesToDtoArray(kind, pq.countEntities(), pq.asIterable(withLimit(to - from + 1).offset(from)), false);
	}

	private PreparedQuery getFiltered(final String kind, final String field, final FilterOperator operator, final Object value) {
		final Query nameQuery = new Query(kind);
		nameQuery.addFilter(field, operator, value);
		return db.prepare(nameQuery);
	}

	@Override
	public Dto getByName(String kind, String name) {
		final PreparedQuery pq = getFiltered(kind, "name", FilterOperator.EQUAL, name);
		return copy.entityToDto(kind, pq.asSingleEntity(), false, false);
	}

	@Override
	public ListQueryResult getAllAssignedTo(String kind, long assignedTo, int from, int to) {
		final PreparedQuery pq = getFiltered(kind, "assignedTo", FilterOperator.EQUAL, assignedTo);
		return copy.entitiesToDtoArray(kind, pq.countEntities(), pq.asIterable(), false);
	}

	@Override
	public ListQueryResult getAllMarked(String kind, int from, int to) {
		final PreparedQuery pq = getFiltered(kind, "marked", FilterOperator.EQUAL, true);
		return copy.entitiesToDtoArray(kind, pq.countEntities(), pq.asIterable(), false);
	}

	@Override
	public ListQueryResult getAllByNamePrefix(String dtoIndex, String prefix, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult search(String dtoIndex, Dto searchContact, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult fulltextSearch(String query, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult getAllRelated(String originating, Long id, String related) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ListQueryResult> getAllRelated(Long id, String relatedDtoIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult fulltextSearchForModule(String dtoIndex, String query, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

}
