package honeycrm.server;

import honeycrm.server.domain.Bean;
import honeycrm.server.transfer.ReflectionHelper;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DomainClassRegistry {
	public static final DomainClassRegistry instance = new DomainClassRegistry();
	private final Map<String, Class<? extends Bean>> dtoToDomain = new HashMap<String, Class<? extends Bean>>();
	private final Map<Class<? extends Bean>, String> domainToDto = new HashMap<Class<? extends Bean>, String>();

	private DomainClassRegistry() {
		try {
			for (final Class<? extends Bean> domainClass : (Class<? extends Bean>[]) ReflectionHelper.getClasses("honeycrm.server.domain")) {
				if (!domainClass.isInterface() || !Modifier.isAbstract(domainClass.getModifiers())) {
					final String name = domainClass.getSimpleName().toLowerCase();
					dtoToDomain.put(name, domainClass);
				}
			}
			
			for (final String dto : dtoToDomain.keySet()) {
				domainToDto.put(dtoToDomain.get(dto), dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot initialize " + DomainClassRegistry.class.toString() + " class");
		}
	}

	public String getDto(final Class<? extends Bean> domainClass) {
		assert domainToDto.containsKey(domainClass);
		return domainToDto.get(domainClass);
	}

	public Class<? extends Bean> getDomain(final String dto) {
		assert dtoToDomain.containsKey(dto);
		return dtoToDomain.get(dto);
	}

	public Set<Class<? extends Bean>> getDomainClasses() {
		return domainToDto.keySet();
	}
}
