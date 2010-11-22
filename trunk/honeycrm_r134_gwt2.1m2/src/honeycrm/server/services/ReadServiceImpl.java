package honeycrm.server.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.services.ReadService;
import honeycrm.server.NewDtoWizard;
import honeycrm.server.domain.Employee;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

public class ReadServiceImpl extends NewService implements ReadService {
	private static final long serialVersionUID = 4925571929249643554L;
	private static final Logger log = Logger.getLogger(ReadServiceImpl.class.getSimpleName());

	// do not use memcache because the objects we want to cache are not serializable.
	public static final boolean ignoreCase = true;
	private static final long CACHE_EXPIRATION = 60 * 1000;
	private final Map<String, List<Entity>> cache = new HashMap<String, List<Entity>>();
	private long lastCacheUpdate = 0;

	// TODO only transmit required fields for listview - do not copy all fields into dto to save bandwidth
	@Override
	public ListQueryResult getAll(String kind, final int from, final int to) {
		final PreparedQuery pq = db.prepare(new Query(kind));
		return copy.entitiesToDtoArray(kind, pq.countEntities(withDefaults()), pq.asIterable(withLimit(to - from + 1).offset(from)), false);
	}

	private PreparedQuery getFiltered(final String kind, final String field, final FilterOperator operator, final Object value) {
		final Query nameQuery = new Query(kind);
		nameQuery.addFilter(field, operator, value);
		return db.prepare(nameQuery);
	}

	@Override
	public Dto getByName(String kind, String name) {
		final PreparedQuery pq = getFiltered(kind, "name", FilterOperator.EQUAL, name);
		// TODO should still indicate that nothing has to be resolved
		return copy.entityToDto(kind, pq.asSingleEntity(), false/*, false*/);
	}

	@Override
	public ListQueryResult getAllAssignedTo(String kind, long assignedTo, int from, int to) {
		final PreparedQuery pq = getFiltered(kind, "assignedTo", FilterOperator.EQUAL, KeyFactory.createKey(Employee.class.getSimpleName(), assignedTo));
		return copy.entitiesToDtoArray(kind, pq.countEntities(withDefaults()), pq.asIterable(), false);
	}

	@Override
	public ListQueryResult getAllMarked(String kind, int from, int to) {
		final PreparedQuery pq = getFiltered(kind, "marked", FilterOperator.EQUAL, true);
		return copy.entitiesToDtoArray(kind, pq.countEntities(withDefaults()), pq.asIterable(), false);
	}

	@Override
	public ListQueryResult search(String dtoIndex, Dto searchContact, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult getAllRelated(String originating, Long id, String related) {
		if (null == id || id < 1) {
			log.warning("Invalid Id: " + id);
			throw new RuntimeException("Invalid Id: " + id);
		}
		
		final ArrayList<Dto> result = new ArrayList<Dto>();
		
		/**
		 * Get all related entities where the id fields contain the id of the originating entity. E.g. return all contacts which have accountID == 23 where is the id of the originating account.
		 */
		final HashMap<String, HashMap<String, HashSet<String>>> r = NewDtoWizard.getConfiguration().getRelationships();

		if (r.get(originating).containsKey(related)) {
			final Key keyOrigin = KeyFactory.createKey(related, id);

			for (final String fieldName : r.get(originating).get(related)) {
				// TODO use parallel fetch instead if possible
				final PreparedQuery pq = getFiltered(originating, fieldName, FilterOperator.EQUAL, keyOrigin);

				for (final Entity entity : pq.asIterable()) {
					// TODO should still indicate that nothing has to be resolved
					result.add(copy.entityToDto(originating, entity, false/*, false*/));
				}
			}
		}

		return new ListQueryResult(result.toArray(new Dto[0]), result.size());
	}

	@Override
	public Map<String, ListQueryResult> getAllRelated(Long id, String related) {
		final HashMap<String, ListQueryResult> map = new HashMap<String, ListQueryResult>();
		final HashMap<String, HashMap<String, HashSet<String>>> r = NewDtoWizard.getConfiguration().getRelationships();

		for (final String originating : r.keySet()) {
			if (r.get(originating).containsKey(related)) {
				map.put(originating, getAllRelated(originating, id, related));
			}
		}

		return map;
	}

	@Override
	public ListQueryResult getAllByNamePrefix(String kind, String prefix, int from, int to) {
		final ArrayList<Dto> hits = new ArrayList<Dto>();
		final PreparedQuery pq = getFiltered(kind, "name", FilterOperator.GREATER_THAN_OR_EQUAL, prefix);

		for (final Entity entity : pq.asIterable()) {
			if (null != entity.getProperty("name") && entity.getProperty("name") instanceof String && entity.getProperty("name").toString().startsWith(prefix)) {
				// TODO should still indicate that nothing has to be resolved
				hits.add(copy.entityToDto(kind, entity, false/*, false*/));
			}
		}

		return new ListQueryResult(hits.toArray(new Dto[0]), hits.size());
	}

	private boolean isCacheOutOfDate() {
		final long diff = System.currentTimeMillis() - lastCacheUpdate;
		return diff > CACHE_EXPIRATION;
	}

	private List<Entity> getCachedList(final String kind) {
		if (isCacheOutOfDate() || !cache.containsKey(kind)) {
			final PreparedQuery pq = db.prepare(new Query(kind));
			cache.put(kind, pq.asList(withDefaults()));
			lastCacheUpdate = System.currentTimeMillis();
			log.info("fulltext search cache updated");
		}

		return cache.get(kind);
	}

	private ArrayList<Dto> internalFulltextSearchForModule(String kind, String query) {
		final ArrayList<Dto> hits = new ArrayList<Dto>();

		try {
			// performance measurements:
			// Query.execute() ~10%
			// Iterator.next() ~36% (this includes >100k AbstractEntity.jdoReplaceFields() methods very early)

			ENTITY_LOOP: for (final Entity entity : getCachedList(kind)) {
				for (final String field : configuration.get(kind).getFulltextFields()) {
					final String value = (String) entity.getProperty(field); // assume that field type is string

					if (null != value && ((!ignoreCase && value.contains(query)) || (ignoreCase && value.toLowerCase().contains(query.toLowerCase())))) {
						// TODO should still indicate that nothing has to be resolved
						hits.add(copy.entityToDto(kind, entity, false/*, false*/));
						continue ENTITY_LOOP;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("fulltextSearchForModule failed with an exception: " + e.toString());
		}
		return hits;
	}

	@Override
	public ListQueryResult fulltextSearchForModule(String kind, String query, int from, int to) {
		final ArrayList<Dto> hits = internalFulltextSearchForModule(kind, query);
		return new ListQueryResult(hits.toArray(new Dto[0]), hits.size());
	}

	@Override
	public ListQueryResult fulltextSearch(String query, int from, int to) {
		final ArrayList<Dto> hits = new ArrayList<Dto>();
		for (final String kind : configuration.keySet()) {
			if (configuration.get(kind).getFulltextFields().length > 0) {
				hits.addAll(internalFulltextSearchForModule(kind, query));
			}
		}
		return new ListQueryResult(hits.toArray(new Dto[0]), hits.size());
	}

	@Override
	public HashMap<String, ListQueryResult> getAllAssignedTo(long employeeID, int from, int to) {
		final HashMap<String, ListQueryResult> map = new HashMap<String, ListQueryResult>();
		for (final String kind: configuration.keySet()) {
			map.put(kind, getAllAssignedTo(kind, employeeID, from, to));
		}
		return map;
	}
}
