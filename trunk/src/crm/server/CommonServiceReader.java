package crm.server;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

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

	public ListQueryResult<? extends Viewable> getAll(final int dtoIndex, int from, int to) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		query.setRange(from, to);

		return new ListQueryResult<AbstractDto>(getArrayFromQueryResult(dtoIndex, (Collection) query.execute()), getItemCount(dtoIndex));
	}

	public Viewable get(final int dtoIndex, final long id) {
		final Object domainObject = getDomainObject(dtoIndex, id);

		if (null == domainObject) {
			return null;
		} else {
			return (Viewable) copy.copy(domainObject, getDtoClass(dtoIndex));
		}
	}

	public ListQueryResult<? extends Viewable> search(int dtoIndex, Viewable searchDto, int from, int to) {
		return searchWithOperator(dtoIndex, searchDto, from, to, BoolOperator.AND);
	}

	private ListQueryResult<? extends Viewable> searchWithOperator(int dtoIndex, Viewable searchDto, int from, int to, final BoolOperator operator) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final List<String> queries = new LinkedList<String>();
		AbstractDto[] array;

		try {
			for (final Field field : ReflectionHelper.getDtoFields(dtoClass)) {
				final Method getter = dtoClass.getMethod(ReflectionHelper.getMethodName("get", field));
				final Object value = getter.invoke(searchDto);

				if (null != value) {
					if (String.class == field.getType()) { // use ignore case starts with
						// TODO to lower case
						// TODO does this allow sql injection or is it prevented by gwt?
						queries.add(field.getName() + ".startsWith(\"" + value + "\")");
					} else if (Long.class == field.getType() || long.class == field.getType()) { // use exact match
						if (0 != (Long) value) {
							queries.add(field.getName() + " == " + value);
						}
					} else {

					}
				}
			}

			query.setFilter(join(queries, " " + operator.toString() + " "));
			array = getArrayFromQueryResult(dtoIndex, (Collection) query.execute());
		} catch (Exception e) {
			// something went wrong, print stacktrace and return an empty list to the client.
			e.printStackTrace();
			array = (AbstractDto[]) Array.newInstance(dtoClass, 0);
		}

		return new ListQueryResult<AbstractDto>(array, array.length);
	}

	private String join(final List<String> list, final String glue) {
		String query = "";

		for (int i = 0; i < list.size(); i++) {
			query += list.get(i);

			if (i < list.size() - 1) {
				query += glue;
			}
		}

		return query;
	}

	public ListQueryResult<? extends Viewable> getAllByNamePrefix(int dtoIndex, String prefix, int from, int to) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex), "name.startsWith(searchedName)"); // TODO use toLowerCase in query as well to ignore case
		query.declareParameters("String searchedName");
		query.setRange(from, to);
		final Collection collection = (Collection) query.execute(prefix);

		return new ListQueryResult<AbstractDto>(getArrayFromQueryResult(dtoIndex, collection), collection.size());
	}

	public Viewable getByName(int dtoIndex, String name) {
		final Class<? extends AbstractDto> dtoClass = getDtoClass(dtoIndex);
		final Query query = m.newQuery(getDomainClass(dtoIndex), "name == \"" + name + "\"");
		final Collection collection = (Collection) query.execute();

		if (1 == collection.size()) {
			return (Viewable) copy.copy(collection.iterator().next(), getDtoClass(dtoIndex));
		} else if (collection.isEmpty()) {
			return null;
		} else {
			System.err.println("Search ambigious, expected one result. received multiple. Returning first result.");
			return (Viewable) copy.copy(collection.iterator().next(), getDtoClass(dtoIndex));
		}
	}

	public ListQueryResult<? extends Viewable> fulltextSearch(String query, int from, int to) {
		return null;
		/*
		for (final Class<? extends Viewable> dto : dtoToDomainClass.values()) {
			try {
				Viewable viewable = dto.newInstance();
				
				ReflectionHelper.getDtoFields(dto);
				
				dto.getde
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	enum BoolOperator {
		AND {
			@Override
			public String toString() {
				return " AND ";
			}
		},
		OR {
			@Override
			public String toString() {
				return " OR ";
			}
		};
	}

}
