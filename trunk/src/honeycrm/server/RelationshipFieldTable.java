package honeycrm.server;

import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.RelatesTo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RelationshipFieldTable {
	private static final ReflectionHelper reflectionHelper = new ReflectionHelper();
	public static final RelationshipFieldTable instance = new RelationshipFieldTable();
	private final Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();

	private RelationshipFieldTable() {
		for (final Class<? extends AbstractEntity> originatingDtoClass : DomainClassRegistry.instance.getDomainClasses()) {
			for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(originatingDtoClass, RelatesTo.class)) {
				final Class<? extends AbstractEntity> relatedDtoClass = field.getAnnotation(RelatesTo.class).value();

				final String strOrigin = DomainClassRegistry.instance.getDto(originatingDtoClass);
				final String strRelated = DomainClassRegistry.instance.getDto(relatedDtoClass);
				
				if (map.containsKey(strOrigin)) {
					if (map.get(strOrigin).containsKey(strRelated)) {
						map.get(strOrigin).get(strRelated).add(field.getName());
					} else {
						final Set<String> set = new HashSet<String>();
						set.add(field.getName());

						map.get(strOrigin).put(strRelated, set);
					}
				} else {
					final Set<String> set = new HashSet<String>();
					set.add(field.getName());

					final Map<String, Set<String>> relatedDtoMap = new HashMap<String, Set<String>>();
					relatedDtoMap.put(strRelated, set);

					map.put(strOrigin, relatedDtoMap);
				}
			}
		}

		System.out.println("Map contains " + map.size() + " elements");
	}

	public Set<String> getRelationshipFieldNames(final String originatingDto, final String relatedDto) {
		if (map.containsKey(originatingDto) && map.get(originatingDto).containsKey(relatedDto)) {
			return map.get(originatingDto).get(relatedDto);
		} else {
			// return an empty set since no information exists for this combination of originating and related dto
			return new HashSet<String>();
		}
	}
}
