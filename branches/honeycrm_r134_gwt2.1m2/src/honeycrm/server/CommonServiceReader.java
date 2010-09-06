package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.server.domain.Bean;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.Query;

/**
 * Is part of the database layer.
 */
// TODO this throws ConcurrentModificationExceptions (e.g. during getAll).
// TODO every modification should be wrapped into a transaction to avoid this. not sure if this
// would solve the issue.
public class CommonServiceReader extends AbstractCommonService {
	private static final long serialVersionUID = 5202932343066860591L;

	protected Dto[] getArrayFromQueryResult(final String dtoIndex, final Collection<Bean> collection) {
		if (collection.isEmpty()) {
			return null;
		}

		final List<Dto> list = new LinkedList<Dto>();

		for (final Bean item : collection) {
			final Dto dto = copy.copy(item); // 60%

			resolveRelatedEntities(item, dto); // 20%

			list.add(dto);
		}

		return list.toArray(new Dto[0]);
	}

	public static void resolveRelatedEntities(final Bean item, final Dto dto) {
		try {
			final Class<?> originatingClass = item.getClass();

			/**
			 * iterate over all fields annotated as being relate fields.
			 */
			for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(originatingClass, FieldRelateAnnotation.class)) {
				final Class<? extends Bean> relatedClass = field.getAnnotation(FieldRelateAnnotation.class).value();
				final String relatedModuleName = relatedClass.getSimpleName().toLowerCase();

				final Long id = (Long) field.get(item);

				if (null != id && id > 0) {
					/**
					 * retrieve the referenced entity and copy its dto representation as an additional field into the originating dto object.
					 */
					final Bean relatedEntity = getDomainObject(relatedModuleName, id);

					if (null != relatedEntity) {
						final Dto relatedDto = copy.copy(relatedEntity);
						dto.set(field.getName() + "_resolved", relatedDto);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getItemCount(final String dtoIndex) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		query.setResult("count(this)");
		final Object result = query.execute();

		if (result instanceof Integer) {
			return (Integer) query.execute();
		} else {
			// assume that something else can be returned if an error occured (or table is empty
			// (?))
			return 0;
		}
	}

	public ListQueryResult getAll(final String dtoIndex, int from, int to) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		// order by number of views descending -> most viewed items at the top
		// TODO allow user defined order as well i.e. sorting by arbitrary columns
		query.setOrdering("views desc");
		query.setRange(from, to);

		return new ListQueryResult(getArrayFromQueryResult(dtoIndex, (Collection) query.execute()), getItemCount(dtoIndex));
	}

	public Dto get(final String dtoIndex, final long id) {
		final Bean domainObject = getDomainObject(dtoIndex, id);

		if (null == domainObject) {
			return null;
		} else {
			final Dto dto = copy.copy(domainObject);
			resolveRelatedEntities(domainObject, dto);
			return dto;
		}
	}

	public ListQueryResult search(String dtoIndex, Dto searchDto, int from, int to) {
		return searchWithOperator(dtoIndex, searchDto, from, to, BoolOperator.AND);
	}

	protected ListQueryResult searchWithOperator(String dtoIndex, Dto searchDto, int from, int to, final BoolOperator operator) {
		// TODO
		return getAll(dtoIndex, from, to);
		/*
		 * if (null == searchDto) // cannot do a proper search, do a getAll query instead // however, the question remains why we received a searchDto which is null.. return getAll(dtoIndex, from, to);
		 * 
		 * final Query query = m.newQuery(getDomainClass(dtoIndex)); final Class<Dto> dtoClass = getDtoClass(dtoIndex); final List<String> queries = new LinkedList<String>(); Dto[] array;
		 * 
		 * try { for (final Field field : reflectionHelper.getDtoFields(dtoClass)) { final Method getter = dtoClass.getMethod(reflectionHelper.getMethodName("get", field)); final Object value = getter.invoke(searchDto); }
		 * 
		 * if (null != value) { if (String.class == field.getType()) { // use ignore case starts with // TODO to lower case // TODO does this allow sql injection or is it prevented by gwt? queries.add("this." + field.getName() + ".startsWith(\"" + value.toString() + "\")"); } else if (Long.class == field.getType() || long.class == field.getType()) { // use // exact // match if (0 != (Long) value) { queries.add(field.getName() + " == " + value); } } else {
		 * 
		 * } } } query.setFilter(CollectionHelper.join(queries, operator.toString())); array = getArrayFromQueryResult(dtoIndex, (Collection) query.execute()); } catch (Exception e) { // something went wrong, print stacktrace and return an empty list to the client. e.printStackTrace(); array = (Dto[]) Array.newInstance(dtoClass, 0); }
		 * 
		 * return new ListQueryResult(array, array.length);
		 */
	}

	public ListQueryResult getAllByNamePrefix(String dtoIndex, String prefix, int from, int to) {
		if (prefix.trim().isEmpty()) {
			return new ListQueryResult(new Dto[0], 0);
		} else {
			try {
				// TODO make sure the domain object has a name field before searching on it
				final Query query = m.newQuery(getDomainClass(dtoIndex), "name.startsWith(searchedName)");
				query.declareParameters("String searchedName");
				query.setRange(from, to);

				final Collection collection = (Collection) query.execute(prefix);
				System.out.println("getAllByNamePrefix('" + prefix + "')");
				return new ListQueryResult(getArrayFromQueryResult(dtoIndex, collection), collection.size());
			} catch (RuntimeException e) {
				log.warning("Exception occured during getAllByNamePrefix(" + dtoIndex + "," + prefix + "," + from + "," + to + ")");
				return new ListQueryResult(new Dto[0], 0);
			}
		}
	}

	public Dto getByName(String dtoIndex, String name) {
		final Query query = m.newQuery(getDomainClass(dtoIndex), "name == \"" + name + "\"");
		final Collection<Bean> collection = (Collection<Bean>) query.execute();

		if (1 == collection.size()) {
			return copy.copy(collection.iterator().next());
		} else if (collection.isEmpty()) {
			return null;
		} else {
			System.err.println("Search ambigious, expected one result. received multiple. Returning first result.");
			return copy.copy(collection.iterator().next());
		}
	}

	// TODO do not implement fulltext search using compass / lucene until it runs when deployed in
	// app engine
	/*
	 * public ListQueryResult<? extends AbstractDto> fulltextSearch(final String query, int from, int to) { final ListQueryResult<AbstractDto> result = new ListQueryResult<AbstractDto>();
	 * 
	 * if (null != query && query.length() > FulltextSearchWidget.MIN_QUERY_LENGTH) { System.out.println("searching for '" + query + "'");
	 * 
	 * final CompassSearchSession session = PMF.compass().openSearchSession(); CompassHits hits = session.find(query);
	 * 
	 * log.info("got " + hits.getLength() + " results"); System.out.println("got " + hits.getLength() + " results");
	 * 
	 * if (0 < hits.getLength()) { final AbstractDto[] dtos = new AbstractDto[hits.getLength()];
	 * 
	 * for (int i=0; i<hits.getLength(); i++) { final CompassHit hit = hits.hit(i); dtos[i] = (AbstractDto) copy.copy(hit.getData(), domainClassToDto.get(hit.getData().getClass())); }
	 * 
	 * session.close(); return new ListQueryResult<AbstractDto>(dtos, dtos.length); }
	 * 
	 * session.close(); }
	 * 
	 * return result; }
	 */

	public ListQueryResult getAllMarked(String dtoIndex, int from, int to) {
		return getAllFiltered(dtoIndex, "marked == true", from, to);
	}

	private ListQueryResult getAllRelated(String originating, Long id, String related) {
		final List<Bean> result = new ArrayList<Bean>();
		/**
		 * Get all related entities where the id fields contain the id of the originating entity e.g. return all contacts which have accountID == 23 where is the id of the originating account.
		 */
		// final long dbStart = System.currentTimeMillis();

		for (final String fieldName : RelationshipFieldTable.instance.getRelationshipFieldNames(originating, related)) {
			final Query q = m.newQuery(getDomainClass(originating));
			q.setFilter(fieldName + " == " + id);
			try {
				result.addAll((Collection<Bean>) q.execute());
			} catch (RuntimeException e) {
				System.err.println("runtime exc");
			}
		}

		// final long dbDiff = System.currentTimeMillis() - dbStart;

		// final long convertStart = System.currentTimeMillis();
		final ListQueryResult r = new ListQueryResult(getArrayFromQueryResult(originating, result), result.size());
		// final long convertDiff = System.currentTimeMillis() - convertStart;

		return r;
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

	// TODO make this faster: getting account data takes 27ms. getting empty relationship data for an account takes 270ms.
	public Map<String, ListQueryResult> getAllRelated(final Long id, final String related) {
		final Map<String, ListQueryResult> map = new HashMap<String, ListQueryResult>();
		final Map<String, Map<String, Set<String>>> relations = RelationshipFieldTable.instance.getMap();

		for (final String originating : relations.keySet()) {
			if (relations.get(originating).containsKey(related)) {
				map.put(originating, getAllRelated(originating, id, related));
			}
		}

		return map;
	}

	public ListQueryResult getAllAssignedTo(String dtoIndex, long employeeID, int from, int to) {
		return getAllFiltered(dtoIndex, "assignedTo == " + employeeID, from, to);
	}

	private ListQueryResult getAllFiltered(final String dtoIndex, final String filters, final int from, final int to) {
		final Query query = m.newQuery(getDomainClass(dtoIndex));
		query.setRange(from, to);
		query.setFilter(filters);

		final Collection<Bean> collection = (Collection<Bean>) query.execute();
		return new ListQueryResult(getArrayFromQueryResult(dtoIndex, collection), collection.size());
	}
}
