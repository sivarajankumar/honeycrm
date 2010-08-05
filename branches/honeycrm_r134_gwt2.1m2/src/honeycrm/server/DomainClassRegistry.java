package honeycrm.server;

import honeycrm.server.domain.AbstractEntity;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DomainClassRegistry {
	public static final DomainClassRegistry instance = new DomainClassRegistry();
	private final Map<String, Class<? extends AbstractEntity>> dtoToDomain = new HashMap<String, Class<? extends AbstractEntity>>();
	private final Map<Class<? extends AbstractEntity>, String> domainToDto = new HashMap<Class<? extends AbstractEntity>, String>();

	private DomainClassRegistry() {
		try {
			for (final Class<AbstractEntity> domainClass : (Class<AbstractEntity>[]) ReflectionHelper.getClasses("honeycrm.server.domain")) {
				if (!Modifier.isAbstract(domainClass.getModifiers())) {
					final String name = domainClass.getSimpleName().toLowerCase();
					dtoToDomain.put(name, domainClass);
				}
			}

			for (final String dto : dtoToDomain.keySet()) {
				domainToDto.put(dtoToDomain.get(dto), dto);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot initialize " + DomainClassRegistry.class.toString() + " class");
		}
	}

	public String getDto(final Class<? extends AbstractEntity> domainClass) {
		assert domainToDto.containsKey(domainClass);
		return domainToDto.get(domainClass);
	}

	public Class<? extends AbstractEntity> getDomain(final String dto) {
		assert dtoToDomain.containsKey(dto);
		return dtoToDomain.get(dto);
	}

	public Set<Class<? extends AbstractEntity>> getDomainClasses() {
		return domainToDto.keySet();
	}
}
