package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.transfer.DtoWizard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jdo.Query;

public class CommonServiceReaderFulltext extends AbstractCommonService {
	public static final boolean ignoreCase = true;
	private static final long serialVersionUID = -7000384067604090223L;
	private static final Map<Class<? extends AbstractEntity>, Field[]> searchableFields = DtoWizard.instance.getSearchableFields();

	public ListQueryResult fulltextSearch(final String query, int from, int to) {
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
		final List<Dto> moduleList = new ArrayList<Dto>();

		try {
			final Query q = m.newQuery(domainClass);

			/**
			 * performance measurements: 
			 * Query.execute() ~10%  
			 * Iterator.next() ~36% (this includes >100k AbstractEntity.jdoReplaceFields() methods very early) 
			 */
			final List<AbstractEntity> list = new ArrayList<AbstractEntity>();
			list.addAll((Collection<? extends AbstractEntity>) q.execute());
			// TODO does this read the values at bulk instead of step by step i.e. is this faster than iterating using Iterator.next()
			
			ENTITY_LOOP: for (final AbstractEntity entity : list) {
				for (final Field field : searchableFields.get(domainClass)) {
					final String value = (String) field.get(entity); // assume that field type is string

					if (null != value && ((!ignoreCase && value.contains(query)) || (ignoreCase && value.toLowerCase().contains(query.toLowerCase())))) {
						moduleList.add(copy.copy(entity));
						continue ENTITY_LOOP;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return moduleList;
	}

	public ListQueryResult fulltextSearchForModule(String dtoIndex, String query, int from, int to) {
		final List<Dto> list = fulltextSearchForModule(query, registry.getDomain(dtoIndex));
		return new ListQueryResult(list.toArray(new Dto[0]), list.size());
	}
}
