package crm.server;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import crm.client.CollectionHelper;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

/**
 * Is part of the database layer.
 */
// TODO this throws ConcurrentModificationExceptions (e.g. during getAll).
// TODO every modification should be wrapped into a transaction to avoid this. not sure if this would solve the issue.
public class CommonServiceReader extends AbstractCommonService {
	private static final long serialVersionUID = 5202932343066860591L;

	private int getItemCount(final int dtoIndex) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		query.setResult("count(this)");
		final Object result = query.execute();

		if (result instanceof Integer) {
			return (Integer) query.execute();
		} else {
			// assume that something else can be returned if an error occured (or table is empty (?))
			return 0;
		}
	}

	public ListQueryResult<? extends AbstractDto> getAll(final int dtoIndex, int from, int to) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		// order by number of views descending -> most viewed items at the top
		// TODO allow user defined order as well i.e. sorting by arbitrary columns
		query.setOrdering("views desc");
		query.setRange(from, to);

		return new ListQueryResult<AbstractDto>(getArrayFromQueryResult(dtoIndex, (Collection) query.execute()), getItemCount(dtoIndex));
	}

	public AbstractDto get(final int dtoIndex, final long id) {
		final Object domainObject = getDomainObject(dtoIndex, id);

		if (null == domainObject) {
			return null;
		} else {
			return (AbstractDto) copy.copy(domainObject, getDtoClass(dtoIndex));
		}
	}

	public ListQueryResult<? extends AbstractDto> search(int dtoIndex, AbstractDto searchDto, int from, int to) {
		return searchWithOperator(dtoIndex, searchDto, from, to, BoolOperator.AND);
	}

	protected ListQueryResult<? extends AbstractDto> searchWithOperator(int dtoIndex, AbstractDto searchDto, int from, int to, final BoolOperator operator) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final List<String> queries = new LinkedList<String>();
		AbstractDto[] array;

		try {
			for (final Field field : reflectionHelper.getDtoFields(dtoClass)) {
				final Method getter = dtoClass.getMethod(reflectionHelper.getMethodName("get", field));
				final Object value = getter.invoke(searchDto);

				if (null != value) {
					if (String.class == field.getType()) { // use ignore case starts with
						// TODO to lower case
						// TODO does this allow sql injection or is it prevented by gwt?
						queries.add("this." + field.getName() + ".startsWith(\"" + value.toString() + "\")");
					} else if (Long.class == field.getType() || long.class == field.getType()) { // use exact match
						if (0 != (Long) value) {
							queries.add(field.getName() + " == " + value);
						}
					} else {

					}
				}
			}
			query.setFilter(CollectionHelper.join(queries, operator.toString()));
			array = getArrayFromQueryResult(dtoIndex, (Collection) query.execute());
		} catch (Exception e) {
			// something went wrong, print stacktrace and return an empty list to the client.
			e.printStackTrace();
			array = (AbstractDto[]) Array.newInstance(dtoClass, 0);
		}

		return new ListQueryResult<AbstractDto>(array, array.length);
	}

	public ListQueryResult<? extends AbstractDto> getAllByNamePrefix(int dtoIndex, String prefix, int from, int to) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex), "name.startsWith(searchedName)"); // TODO use toLowerCase in query as well to ignore case
		query.declareParameters("String searchedName");
		query.setRange(from, to);

		final Collection collection = (Collection) query.execute(prefix);

		return new ListQueryResult<AbstractDto>(getArrayFromQueryResult(dtoIndex, collection), collection.size());
	}

	public AbstractDto getByName(int dtoIndex, String name) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex), "name == \"" + name + "\"");
		final Collection collection = (Collection) query.execute();

		if (1 == collection.size()) {
			return (AbstractDto) copy.copy(collection.iterator().next(), getDtoClass(dtoIndex));
		} else if (collection.isEmpty()) {
			return null;
		} else {
			System.err.println("Search ambigious, expected one result. received multiple. Returning first result.");
			return (AbstractDto) copy.copy(collection.iterator().next(), getDtoClass(dtoIndex));
		}
	}

	// TODO do not implement fulltext search using compass / lucene until it runs when deployed in app engine
/*	public ListQueryResult<? extends AbstractDto> fulltextSearch(final String query, int from, int to) {
		final ListQueryResult<AbstractDto> result = new ListQueryResult<AbstractDto>();
		
		if (null != query && query.length() > FulltextSearchWidget.MIN_QUERY_LENGTH) {
			System.out.println("searching for '" + query + "'");

			final CompassSearchSession session = PMF.compass().openSearchSession();
			CompassHits hits = session.find(query);
			
			log.info("got " + hits.getLength() + " results");
			System.out.println("got " + hits.getLength() + " results");

			if (0 < hits.getLength()) {
				final AbstractDto[] dtos = new AbstractDto[hits.getLength()];

				for (int i=0; i<hits.getLength(); i++) {
					final CompassHit hit = hits.hit(i);
					dtos[i] = (AbstractDto) copy.copy(hit.getData(), domainClassToDto.get(hit.getData().getClass()));
				}

				session.close();
				return new ListQueryResult<AbstractDto>(dtos, dtos.length);
			}
				
			session.close();
		}
		
		return result;
	}*/

	public ListQueryResult<? extends AbstractDto> getAllMarked(int dtoIndex, int from, int to) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		query.setRange(from, to);
		query.setFilter("marked == true");

		final Collection collection = (Collection) query.execute();
		return new ListQueryResult<AbstractDto>(getArrayFromQueryResult(dtoIndex, collection), collection.size());
	}

	enum BoolOperator {
		AND {
			@Override
			public String toString() {
				return " && ";
			}
		},
		OR {
			@Override
			public String toString() {
				return " || ";
			}
		};
	}

}
