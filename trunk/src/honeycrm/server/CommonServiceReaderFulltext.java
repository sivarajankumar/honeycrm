package honeycrm.server;

import honeycrm.client.IANA;
import honeycrm.client.dto.AbstractDto;
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

	public ListQueryResult<? extends AbstractDto> fulltextSearch(final String query, int from, int to) {
		final List<AbstractDto> list = new LinkedList<AbstractDto>();

		try {
			for (final Class<? extends AbstractEntity> domainClass : registry.getDomainClasses()) {
				list.addAll(fulltextSearchForModule(query, domainClass));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ListQueryResult<AbstractDto>(list.toArray(new AbstractDto[0]), list.size());
	}

	private List<AbstractDto> fulltextSearchForModule(final String query, final Class<? extends AbstractEntity> domainClass) {
		final List<AbstractDto> moduleList = new ArrayList<AbstractDto>();

		try {
			final Query q = m.newQuery(domainClass);

			ENTITY_LOOP: for (final AbstractEntity entity : (Collection<? extends AbstractEntity>) q.execute()) {
				final Class<? extends AbstractEntity> entityClass = entity.getClass();

				for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(entityClass, SearchableProperty.class)) {
					if (String.class == field.getType()) {
						String value = (String) entityClass.getMethod(reflectionHelper.getMethodName("get", field)).invoke(entity);

						if (null != value && ((!ignoreCase && value.contains(query)) || (ignoreCase && value.toLowerCase().contains(query.toLowerCase())))) {
							moduleList.add((AbstractDto) copy.copy(entity, registry.getDto(entityClass)));
							continue ENTITY_LOOP;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return moduleList;
	}

	public ListQueryResult<? extends AbstractDto> fulltextSearchForModule(int dtoIndex, String query, int from, int to) {
		final List<AbstractDto> list = fulltextSearchForModule(query, registry.getDomain(IANA.unmarshal(dtoIndex)));
		return new ListQueryResult<AbstractDto>(list.toArray(new AbstractDto[0]), list.size());
	}
}
