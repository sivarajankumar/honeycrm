/*
 * Copyright 2010 @ashigeru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package honeycrm.server.test.small.dyn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class ResourceStore {

	private static final String PROPERTY_VERSION = "v"; //$NON-NLS-1$

	private static final String PROPERTY_CONTENTS = "c"; //$NON-NLS-1$

	private static final Long CURRENT_VERSION = Long.valueOf(1L);

	private DatastoreService service;

	private String kindName;

	public ResourceStore(DatastoreService service, String kindName) {
		if (service == null) {
			throw new IllegalArgumentException("service must not be null"); //$NON-NLS-1$
		}
		if (kindName == null) {
			throw new IllegalArgumentException("kindName must not be null"); //$NON-NLS-1$
		}
		this.service = service;
		this.kindName = kindName;
	}

	public String getKindName() {
		return this.kindName;
	}

	Key createKey(String path) {
		assert path != null;
		String name = mangle(path);
		return KeyFactory.createKey(getKindName(), name);
	}

	private String mangle(String path) {
		assert path != null;
		return '/' + path;
	}

	public byte[] get(String path) {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null"); //$NON-NLS-1$
		}
		Key key = createKey(path);
		Entity entity;
		try {
			entity = service.get(null, key);
		} catch (EntityNotFoundException e) {
			return null;
		}
		return fromEntity(entity);
	}

	public void put(String path, byte[] contents) {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null"); //$NON-NLS-1$
		}
		if (contents == null) {
			throw new IllegalArgumentException("contents must not be null"); //$NON-NLS-1$
		}
		Entity entity = toEntity(path, contents);
		service.put(null, entity);
	}

	public void delete(String path) {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null"); //$NON-NLS-1$
		}
		Key key = createKey(path);
		service.delete((Transaction) null, key);
	}

	public Map<String, byte[]> get(Iterable<String> paths) {
		if (paths == null) {
			throw new IllegalArgumentException("paths must not be null"); //$NON-NLS-1$
		}
		Map<Key, String> keys = new HashMap<Key, String>();
		for (String path : paths) {
			Key key = createKey(path);
			keys.put(key, path);
		}

		Map<Key, Entity> entities = service.get(null, keys.keySet());
		Map<String, byte[]> contents = new HashMap<String, byte[]>();
		for (Map.Entry<Key, Entity> entry : entities.entrySet()) {
			Entity entity = entry.getValue();
			if (entity != null) {
				byte[] bytes = fromEntity(entity);
				if (bytes != null) {
					contents.put(keys.get(entry.getKey()), bytes);
				}
			}
		}
		return contents;
	}

	public void put(Map<String, byte[]> pathAndContents) {
		if (pathAndContents == null) {
			throw new IllegalArgumentException("pathAndContents must not be null"); //$NON-NLS-1$
		}
		List<Entity> entities = new ArrayList<Entity>();
		for (Map.Entry<String, byte[]> entry : pathAndContents.entrySet()) {
			Entity entity = toEntity(entry.getKey(), entry.getValue());
			entities.add(entity);
		}
		service.put(entities);
	}

	public void delete(Iterable<String> paths) {
		if (paths == null) {
			throw new IllegalArgumentException("paths must not be null"); //$NON-NLS-1$
		}
		List<Key> keys = new ArrayList<Key>();
		for (String path : paths) {
			keys.add(createKey(path));
		}
		service.delete((Transaction) null, keys);
	}

	private byte[] fromEntity(Entity entity) {
		assert entity != null;
		if (entity.hasProperty(PROPERTY_CONTENTS) == false) {
			return null;
		}
		Blob contents = (Blob) entity.getProperty(PROPERTY_CONTENTS);
		return contents.getBytes();
	}

	private Entity toEntity(String path, byte[] contents) {
		assert path != null;
		assert contents != null;
		Key key = createKey(path);
		Entity entity = new Entity(key);
		entity.setUnindexedProperty(PROPERTY_CONTENTS, new Blob(contents));
		entity.setUnindexedProperty(PROPERTY_VERSION, CURRENT_VERSION);
		return entity;
	}
}
