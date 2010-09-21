package honeycrm.server.services;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.services.ReadService;
import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.Contact;
import honeycrm.server.domain.Contract;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.transfer.DtoWizard;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReadServiceImpl extends RemoteServiceServlet implements ReadService {
	private static final long serialVersionUID = 4925571929249643554L;
	private static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	private static final CachingReflectionHelper reflectionHelper = new CachingReflectionHelper();
	private static final Map<Class<?>, Map<Field, Class<?>>> RELATE_FIELDS = DtoWizard.instance.getRelateFields();

	@Override
	public ListQueryResult getAll(String kind) {
		final PreparedQuery pq = db.prepare(new Query(kind));
		return entitiesToDtoArray(kind, pq.countEntities(), pq.asIterable());
	}

	protected ListQueryResult entitiesToDtoArray(String kind, final int count, final Iterable<Entity> entities) {
		int i = 0;
		final Dto[] dtos = new Dto[count];

		for (final Entity entity : entities) {
			dtos[i++] = entityToDto(kind, entity);
		}

		return new ListQueryResult(dtos, count);
	}

	protected Dto entityToDto(String kind, final Entity entity) {
		final Dto dto = new Dto();
		dto.setModule(kind);

		for (final Map.Entry<String, Object> entry : entity.getProperties().entrySet()) {
			dto.set(entry.getKey(), (Serializable) entry.getValue());
		}

		resolveRelatedEntities(dto, entity);
		resolveKeyLists(dto, entity);

		return dto;
	}

	private void resolveKeyLists(Dto dto, Entity entity) {
		try {
			final Class<?> entityClass = Contract.class;
			final Field field = entityClass.getField("services");

			if (RELATE_FIELDS.containsKey(entityClass) && RELATE_FIELDS.get(entityClass).containsKey(field)) {
				final ArrayList<Dto> children = new ArrayList<Dto>();
				final Iterable<Key> keys = (Iterable<Key>) entity.getProperty("services");
				
				if (null != keys) {
					for (final Map.Entry<Key, Entity> entry : db.get(keys).entrySet()) {
						children.add(entityToDto(UniqueService.class.getSimpleName(), entry.getValue()));
					}
				}

				dto.set("services", children);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resolveRelatedEntities(Dto dto, Entity entity) {
		for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(Contact.class, FieldRelateAnnotation.class)) {
			final Class<? extends AbstractEntity> relatedClass = field.getAnnotation(FieldRelateAnnotation.class).value();
			final Long id = (Long) entity.getProperty(field.getName());

			if (null != id && id > 0) {
				/**
				 * retrieve the referenced entity and copy its dto representation as an additional field into the originating dto object.
				 */
				final Dto relatedEntity = get(relatedClass.getSimpleName(), id);

				if (null != relatedEntity) {
					dto.set(field.getName() + "_resolved", relatedEntity);
				}
			}
		}
	}

	@Override
	public Dto get(String kind, long id) {
		try {
			return entityToDto(kind, db.get(KeyFactory.createKey(kind, id)));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Dto getByName(String kind, String name) {
		final Query nameQuery = new Query(kind);
		nameQuery.addFilter("name", FilterOperator.GREATER_THAN_OR_EQUAL, name);
		final PreparedQuery pq = db.prepare(nameQuery);

		return entityToDto(kind, pq.asSingleEntity());
	}

	@Override
	public ListQueryResult getAllAssignedTo(String dtoIndex, long employeeID, int from, int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListQueryResult getAllMarked(String dtoIndex, int from, int to) {
		// TODO Auto-generated method stub
		return null;
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
