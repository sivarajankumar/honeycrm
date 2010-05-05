package crm.client;

import java.util.HashMap;
import java.util.Map;

import crm.client.dto.AbstractDto;
import crm.client.dto.DtoAccount;
import crm.client.dto.DtoContact;
import crm.client.dto.DtoEmployee;

// Since GWT cannot (un-)marshal Class<T> constructs this is useful for the implementation of a generic service (e.g. CommonServiceImpl)
// to communicate the type of the expected objects (e.g. the getAll call specifies the type of the objects that should be retrieved as the first parameter).
public class IANA {
	private static final Map<Class<? extends AbstractDto>, Integer> resolveMap = new HashMap<Class<? extends AbstractDto>, Integer>();

	static {
		int i = 0;

		resolveMap.put(DtoAccount.class, i++);
		resolveMap.put(DtoContact.class, i++);
		resolveMap.put(DtoEmployee.class, i++);
	}

	/**
	 * Returns a number for the given class.
	 */
	public static int mashal(final Class<? extends AbstractDto> clazz) {
		if (resolveMap.containsKey(clazz)) {
			return resolveMap.get(clazz);
		} else {
			throw new RuntimeException("Cannot return number assigned for class " + clazz.getName());
		}
	}

	/**
	 * Returns the class represented by a given number. unmarshal(marshal(A)) = A.
	 */
	public static Class<? extends AbstractDto> unmarshal(final int marshalled) {
		if (resolveMap.containsValue(marshalled)) {
			for (final Class<? extends AbstractDto> clazz : resolveMap.keySet()) {
				if (resolveMap.get(clazz).equals(marshalled)) {
					return clazz;
				}
			}
		}
		throw new RuntimeException("Cannot unmarshal class from marshal value: " + marshalled);
	}
}
