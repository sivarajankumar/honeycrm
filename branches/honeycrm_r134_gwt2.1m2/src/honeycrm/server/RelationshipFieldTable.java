package honeycrm.server;

import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.transfer.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

public class RelationshipFieldTable {
	private static final ReflectionHelper reflectionHelper = new ReflectionHelper();
	public static final RelationshipFieldTable instance = new RelationshipFieldTable();
	private final HashMap<String, HashMap<String, HashSet<String>>> map = new HashMap<String, HashMap<String, HashSet<String>>>();

	private RelationshipFieldTable() {
		for (final Class<? extends AbstractEntity> originatingDtoClass : DomainClassRegistry.instance.getDomainClasses()) {
			for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(originatingDtoClass, FieldRelateAnnotation.class)) {
				final Class<?> relatedDtoClass = field.getAnnotation(FieldRelateAnnotation.class).value();

				final String strOrigin = DomainClassRegistry.instance.getDto(originatingDtoClass);
				final String strRelated = DomainClassRegistry.instance.getDto((Class<? extends AbstractEntity>) relatedDtoClass);

				insertIntoMap(field, strOrigin, strRelated);
			}
		}

		System.out.println("Map contains " + map.size() + " elements");
	}

	private void insertIntoMap(final Field field, final String strOrigin, final String strRelated) {
		if (map.containsKey(strOrigin)) {
			if (map.get(strOrigin).containsKey(strRelated)) {
				map.get(strOrigin).get(strRelated).add(field.getName());
			} else {
				final HashSet<String> set = new HashSet<String>();
				set.add(field.getName());

				map.get(strOrigin).put(strRelated, set);
			}
		} else {
			final HashSet<String> set = new HashSet<String>();
			set.add(field.getName());

			final HashMap<String, HashSet<String>> relatedDtoMap = new HashMap<String, HashSet<String>>();
			relatedDtoMap.put(strRelated, set);

			map.put(strOrigin, relatedDtoMap);
		}
	}

	// TODO write test
	public HashSet<String> getRelationshipFieldNames(final String originatingDto, final String relatedDto) {
		if (map.containsKey(originatingDto) && map.get(originatingDto).containsKey(relatedDto)) {
			return map.get(originatingDto).get(relatedDto);
		} else {
			// return an empty set since no information exists for this combination of originating and related dto
			return new HashSet<String>();
		}
	}

	public HashMap<String, HashMap<String, HashSet<String>>> getMap() {
		return map;
	}
}
