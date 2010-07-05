package honeycrm.server.test;

import honeycrm.server.CachingReflectionHelper;
import honeycrm.server.ReflectionHelper;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.domain.decoration.RelatesTo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class RelatesToTest extends TestCase {
	public void testRelatesToIsSet() {
		try {
			boolean failed = false;

			final ReflectionHelper reflectionHelper = new CachingReflectionHelper();

			for (final Class<? extends AbstractEntity> clazz : ReflectionHelper.getClasses("honeycrm.server.domain")) {
				if (Modifier.isAbstract(clazz.getModifiers())) {
					continue;
				}

				for (final Field field : reflectionHelper.getAllFields(clazz)) {
					final boolean isLong = Long.class.equals(field.getType()) || long.class.equals(field.getType());
					final boolean hasCorrectName = field.getName().toLowerCase().contains("id");
					final boolean isAnnotated = field.isAnnotationPresent(RelatesTo.class);

					if (isLong && hasCorrectName && !isAnnotated) {
						System.err.println(clazz.getSimpleName() + "." + field.getName() + " looks like a foreign key field with no " + RelatesTo.class.getSimpleName() + " annotation set.");
						failed = true;
					}
				}
			}

			if (failed) {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
