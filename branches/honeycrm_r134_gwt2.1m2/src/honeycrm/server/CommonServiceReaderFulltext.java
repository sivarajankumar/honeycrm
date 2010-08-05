package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.server.domain.AbstractEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import org.compass.annotations.SearchableProperty;

public class CommonServiceReaderFulltext extends AbstractCommonService {
	public static final boolean ignoreCase = true;
	private static final long serialVersionUID = -7000384067604090223L;

	public ListQueryResult fulltextSearch(final String query, final int from, final int to) {
		final List<Dto> list = new LinkedList<Dto>();

		try {
			for (final Class<? extends AbstractEntity> domainClass : registry.getDomainClasses()) {
				list.addAll(fulltextSearchForModule(query, domainClass));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return new ListQueryResult(list.toArray(new Dto[0]), list.size());
	}

	private List<Dto> fulltextSearchForModule(final String query, final Class<? extends AbstractEntity> domainClass) {
		final List<Dto> moduleList = new ArrayList<Dto>();

		try {
			final Query q = m.newQuery(domainClass);

			ENTITY_LOOP: for (final AbstractEntity entity : (Collection<? extends AbstractEntity>) q.execute()) {
				final Class<? extends AbstractEntity> entityClass = entity.getClass();

				for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(entityClass, SearchableProperty.class)) {
					if (String.class == field.getType()) {
						final String value = (String) entityClass.getMethod(reflectionHelper.getMethodName("get", field)).invoke(entity);

						if (null != value && ((!ignoreCase && value.contains(query)) || (ignoreCase && value.toLowerCase().contains(query.toLowerCase())))) {
							moduleList.add(copy.copy(entity));
							continue ENTITY_LOOP;
						}
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return moduleList;
	}

	public ListQueryResult fulltextSearchForModule(final String dtoIndex, final String query, final int from, final int to) {
		final List<Dto> list = fulltextSearchForModule(query, registry.getDomain(dtoIndex));
		return new ListQueryResult(list.toArray(new Dto[0]), list.size());
	}
}
