package honeycrm.server.services;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.server.domain.UniqueService;
import honeycrm.server.domainNew.NewDtoWizard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class NewService extends RemoteServiceServlet {
	private static final long serialVersionUID = -4102236506193658058L;
	protected static final DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	protected final CopyMachine copy = new CopyMachine();

	class CopyMachine {
		private final HashMap<String, ModuleDto> configuration = NewDtoWizard.getConfiguration().getModuleDtos();

		public ListQueryResult entitiesToDtoArray(String kind, final int count, final Iterable<Entity> entities, boolean isDetailView) {
			int i = 0;
			final Dto[] dtos = new Dto[count];

			for (final Entity entity : entities) {
				dtos[i++] = entityToDto(kind, entity, isDetailView, true);
			}

			return new ListQueryResult(dtos, count);
		}

		public Dto entityToDto(final String kind, final Entity entity, final boolean isDetailView, final boolean resolveRelatedEntities) {
			if (null == entity) {
				return null;
			}

			final Dto dto = new Dto();
			dto.setModule(kind);
			dto.setId(entity.getKey().getId());

			for (final Map.Entry<String, Object> entry : entity.getProperties().entrySet()) {
				if ("id".equals(entry.getKey())) {
					continue; // skip this field to avoid overriding a field that has already been set
				}

				final String fieldName = entry.getKey();

				if (!isDetailView && !configuration.get(kind).isListViewField(fieldName)) {
					// skip this field because we are in the list view mode but the current field is not visible in the list view.
					// this is used to save bandwidth.
					continue;
				}

				dto.set(fieldName, (Serializable) entry.getValue());
			}

			if (true || resolveRelatedEntities) {
				resolveRelatedEntities(dto, entity);
			}
			if (isDetailView) {
				resolveKeyLists(dto, entity);
			}

			return dto;
		}

		private void resolveKeyLists(Dto dto, Entity entity) {
			try {
				for (final Map.Entry<String, String> oneToManyEntry : configuration.get(dto.getModule()).getOneToManyMappings().entrySet()) {
					final String field = oneToManyEntry.getKey();

					final ArrayList<Dto> children = new ArrayList<Dto>();
					final Iterable<Key> keys = (Iterable<Key>) entity.getProperty(field);

					if (null != keys) {
						for (final Map.Entry<Key, Entity> entry : db.get(keys).entrySet()) {
							final Dto child = entityToDto(UniqueService.class.getSimpleName(), entry.getValue(), false, false);
							int i = 0;
							children.add(child);
						}
					}

					dto.set(field, children);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void resolveRelatedEntities(Dto dto, Entity entity) {
			for (final Map.Entry<String, String> entry : configuration.get(dto.getModule()).getRelateFieldMappings().entrySet()) {
				final String fieldName = entry.getKey();
				final String relatedEntityName = entry.getValue();

				if (null == entity.getProperty(fieldName)) {
					// skip relate fields that are null
				} else if (!(entity.getProperty(fieldName) instanceof Key)) {
					// check if Entity has been stored properly!
					System.err.println("Expected instance of " + Key.class + ". Found " + entity.getProperty(fieldName) + ". Skipping field.");
				} else {
					final long id = ((Key) entity.getProperty(fieldName)).getId();

					if (id > 0) {
						/**
						 * retrieve the referenced entity and copy its dto representation as an additional field into the originating dto object.
						 */
						final Dto relatedEntity = get(relatedEntityName, id);

						if (null != relatedEntity) {
							dto.set(fieldName, relatedEntity.getId());
							dto.set(fieldName + "_resolved", relatedEntity);
						}
					}
				}
			}
		}

		/**
		 * Converts a given Dto to an Entity. This is usually the case during create/update operations.
		 */
		public Entity dtoToEntity(final Dto dto) {
			final boolean entityExists = dto.getId() > 0;
			final Entity entity = entityExists ? new Entity(KeyFactory.createKey(dto.getModule(), dto.getId())) : new Entity(dto.getModule());

			final HashMap<String, String> relationMap = configuration.get(dto.getModule()).getRelateFieldMappings();
			final HashMap<String, String> oneToManyMap = configuration.get(dto.getModule()).getOneToManyMappings();

			for (final Map.Entry<String, Serializable> entry : dto.getAllData().entrySet()) {
				final String fieldName = entry.getKey();

				if (null == entry.getValue()) {
					entity.setProperty(fieldName, null);
				} else if ("id".equals(fieldName)) {
					// Has already been set.
					// This is the id field.
					// Create a Key value for this field and store in instead of the long value.
					// final Key key = KeyFactory.createKey(dto.getModule(), (Long) entry.getValue());
					// entity.setProperty("id", key);
				} else if (relationMap.containsKey(fieldName)) {
					// This is a relate field.
					// Want to store value as Key value. Create a new Key for the referenced entity.
					final String relatedEntity = relationMap.get(fieldName);
					final Key key = KeyFactory.createKey(relatedEntity, (Long) entry.getValue());

					entity.setProperty(fieldName, key);
				} else if (oneToManyMap.containsKey(fieldName)) {
					// This is a one to many field.
					// Create all referenced items and store a list containing their keys in the entity we wanted to create in the first place.
					final ArrayList<Key> keys = new ArrayList<Key>();

					for (final Dto item : (ArrayList<Dto>) entry.getValue()) {
						keys.add(db.put(dtoToEntity(item)));
					}

					entity.setProperty(fieldName, keys);
				} else {
					entity.setProperty(fieldName, entry.getValue());
				}
			}

			return entity;
		}
	}

	public Dto get(String kind, long id) {
		try {
			return copy.entityToDto(kind, db.get(KeyFactory.createKey(kind, id)), true, true);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
