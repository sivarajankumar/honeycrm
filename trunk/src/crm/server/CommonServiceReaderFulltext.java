package crm.server;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import org.compass.annotations.SearchableProperty;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.server.domain.AbstractEntity;

public class CommonServiceReaderFulltext extends AbstractCommonService {
	private static final long serialVersionUID = -7000384067604090223L;

	public ListQueryResult<? extends AbstractDto> fulltextSearch(final String query, int from, int to) {
		final List<AbstractDto> list = new LinkedList<AbstractDto>();

		try {
			for (final Class<? extends AbstractEntity> domainClass : registry.getDomainClasses()) {
				final Query q = m.newQuery(domainClass);

				ENTITY_LOOP: for (final AbstractEntity entity : (Collection<? extends AbstractEntity>) q.execute()) {
					final Class<? extends AbstractEntity> entityClass = entity.getClass();

					for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(entityClass, SearchableProperty.class)) {
						if (String.class == field.getType()) {
							String value = (String) entityClass.getMethod(reflectionHelper.getMethodName("get", field)).invoke(entity);

							if (null != value && value.contains(query)) {
								list.add((AbstractDto) copy.copy(entity, registry.getDto(entityClass)));
								continue ENTITY_LOOP;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ListQueryResult<AbstractDto>(list.toArray(new AbstractDto[0]), list.size());
	}
}
