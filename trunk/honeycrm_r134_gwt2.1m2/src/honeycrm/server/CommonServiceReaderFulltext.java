package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.transfer.DtoWizard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jdo.Query;

public class CommonServiceReaderFulltext extends AbstractCommonService {
	public static final boolean ignoreCase = true;
	private static final long serialVersionUID = -7000384067604090223L;
	private static final Map<Class<? extends AbstractEntity>, Field[]> searchableFields = DtoWizard.instance.getSearchableFields();
	
	// do not use memcache because the objects we want to cache are not serializable.
	private static final Map<Class<?>, Collection<? extends AbstractEntity>> cache = new HashMap<Class<?>, Collection<? extends AbstractEntity>>();
	private static final long CACHE_EXPIRATION = 60 * 1000;
	private long lastCacheUpdate = 0;

	public ListQueryResult fulltextSearch(final String query, int from, int to) {
		log.finest("fulltextSearch('" + query + "')");
		final List<Dto> list = new LinkedList<Dto>();

		try {
			for (final Class<? extends AbstractEntity> domainClass : registry.getDomainClasses()) {
				list.addAll(fulltextSearchForModule(query, domainClass));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ListQueryResult(list.toArray(new Dto[0]), list.size());
	}

	private List<Dto> fulltextSearchForModule(final String query, final Class<? extends AbstractEntity> domainClass) {
		final List<Dto> hits = new ArrayList<Dto>();

		try {
			// performance measurements:
			// Query.execute() ~10%
			// Iterator.next() ~36% (this includes >100k AbstractEntity.jdoReplaceFields() methods very early)

			final Collection<? extends AbstractEntity> list = getCachedList(domainClass);

			ENTITY_LOOP: for (final AbstractEntity entity : list) {
				for (final Field field : searchableFields.get(domainClass /* entity.getClass() */)) {
					final String value = (String) field.get(entity); // assume that field type is string

					if (null != value && ((!ignoreCase && value.contains(query)) || (ignoreCase && value.toLowerCase().contains(query.toLowerCase())))) {
						hits.add(copy.copy(entity));
						continue ENTITY_LOOP;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hits;
	}

	private Collection<? extends AbstractEntity> getCachedList(final Class<? extends AbstractEntity> domainClass) {
		if (isCacheOutOfDate() || !cache.containsKey(domainClass)) {
			final Query q = m.newQuery(domainClass);
			cache.put(domainClass, (Collection<? extends AbstractEntity>) q.execute());
			lastCacheUpdate = System.currentTimeMillis();
			log.info("fulltext search cache update");
		}
		
		return cache.get(domainClass);
	}
	
	private boolean isCacheOutOfDate() {
		final long diff = System.currentTimeMillis() - lastCacheUpdate;
		return diff > CACHE_EXPIRATION;
	}

	public ListQueryResult fulltextSearchForModule(String dtoIndex, String query, int from, int to) {
		final List<Dto> list = fulltextSearchForModule(query, registry.getDomain(dtoIndex));
		return new ListQueryResult(list.toArray(new Dto[0]), list.size());
	}
}
