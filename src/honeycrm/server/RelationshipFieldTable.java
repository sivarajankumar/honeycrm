package honeycrm.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RelationshipFieldTable {
	private static final ReflectionHelper reflectionHelper = new ReflectionHelper();
	public static final RelationshipFieldTable instance = new RelationshipFieldTable();
	private final Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();

	private RelationshipFieldTable() {
		// TODO
/*		for (final Dto originatingDtoClass : DtoRegistry.instance.getDtos()) {
			for (final Field field : reflectionHelper.getAllFieldsWithAnnotation(originatingDtoClass, RelatesTo.class)) {
				final Class<? extends AbstractDto> relatedDtoClass = field.getAnnotation(RelatesTo.class).value();

				if (map.containsKey(originatingDtoClass)) {
					if (map.get(originatingDtoClass).containsKey(relatedDtoClass)) {
						map.get(originatingDtoClass).get(relatedDtoClass).add(field.getName());
					} else {
						final Set<String> set = new HashSet<String>();
						set.add(field.getName());

						map.get(originatingDtoClass).put(relatedDtoClass, set);
					}
				} else {
					final Set<String> set = new HashSet<String>();
					set.add(field.getName());

					final Map<Class<? extends AbstractDto>, Set<String>> relatedDtoMap = new HashMap<Class<? extends AbstractDto>, Set<String>>();
					relatedDtoMap.put(relatedDtoClass, set);

					map.put(originatingDtoClass, relatedDtoMap);
				}
			}
		}*/

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
